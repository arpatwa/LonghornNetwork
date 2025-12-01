//** NOTE: this is about a bit less than 50% of the lab grade covered, there will be a a bit of a stretch to get a 100.
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NetworkLabUI extends JFrame {
    private JComboBox<String> testCaseSelector; //combo box for selecting the testcase
    private JButton runTestsButton;
    private JTextArea testOutputArea;
    private GraphPanel graphPanel;
    private JTextArea roommateArea;
    private JComboBox<String> startStudentSelector;
    private JTextField targetCompanyField;
    private JTextArea referralArea;
    private JTextArea chatHistoryArea;
    private JTextArea studentInfoArea;
    private JTabbedPane tabs;
    private List<List<UniversityStudent>> testCases;

    private static final Color BG_COLOR = Color.decode("#A15032"); //burnt orange
    private static final Color GREEN = Color.decode("#38804E");
    private static final Color TXT = Color.WHITE;


    public NetworkLabUI() {
        super("Longhorn Network Lab UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        // Entire UI background coloe
        getContentPane().setBackground(BG_COLOR);

        // All test cases
        testCases = Arrays.asList(
                socialConnections(Main.generateTestCase1()),
                socialConnections(Main.generateTestCase2()),
                socialConnections(Main.generateTestCase3())
        );

        // UI tab elements
       tabs = new JTabbedPane();
        tabs.addTab("Test Runner", createTestRunnerPanel());
        tabs.addTab("Graph Viewer", createGraphViewerPanel());
        tabs.addTab("Roommate Pairs", createRoommatePanel());
        tabs.addTab("Referral Path", createReferralPanel());
        //tabs.addTab("Student Info", createStudentInfoPanel());
        tabs.addTab("Friends & Chats", createFriendsChatPanel()); // included tab for chats and friends
        add(tabs);
    }

    private JPanel createTestRunnerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel top = new JPanel();
        top.setBackground(BG_COLOR);

        testCaseSelector = new JComboBox<>(new String[]{"Test Case 1", "Test Case 2", "Test Case 3", "All Test Cases"});
        testCaseSelector.setBackground(BG_COLOR);
        testCaseSelector.setForeground(TXT);
        runTestsButton = new JButton("Run Selected Test Case");
        runTestsButton.setBackground(BG_COLOR);
        runTestsButton.setForeground(TXT);

        runTestsButton.addActionListener(e -> onRunTests());
        JLabel testCaseLabel = new JLabel("Selected Test Case:");
        top.add(testCaseLabel);
        testCaseLabel.setForeground(TXT);
        top.add(testCaseSelector);
        top.add(runTestsButton);

        panel.add(top, BorderLayout.NORTH);
        testOutputArea = new JTextArea();
        testOutputArea.setEditable(false);
        testOutputArea.setBackground(BG_COLOR);
        testOutputArea.setForeground(TXT);
        JScrollPane scroll = new JScrollPane(testOutputArea);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createGraphViewerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel controls = new JPanel();
        controls.setBackground(BG_COLOR);
        JComboBox<String> graphCaseSelector = new JComboBox<>(new String[]{"Test Case 1", "Test Case 2", "Test Case 3"});
        JButton loadGraphButton = new JButton("Load Graph");
        loadGraphButton.setBackground(BG_COLOR);
        loadGraphButton.setForeground(TXT);
        graphCaseSelector.setBackground(BG_COLOR);
        graphCaseSelector.setForeground(TXT);

        loadGraphButton.addActionListener(e -> {
            int idx = graphCaseSelector.getSelectedIndex();
            List<UniversityStudent> data = testCases.get(idx);
            StudentGraph graph = new StudentGraph(data);
            graphPanel.setGraph(graph, data);
        });
        controls.add(new JLabel("Select Data:"));
        controls.add(graphCaseSelector);
        controls.add(loadGraphButton);
        panel.add(controls, BorderLayout.NORTH);
        graphPanel = new GraphPanel();
        graphPanel.setBackground(BG_COLOR);
        panel.add(graphPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createRoommatePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel controls = new JPanel();
        controls.setBackground(BG_COLOR);
        JComboBox<String> rmCaseSelector = new JComboBox<>(new String[]{"Test Case 1", "Test Case 2", "Test Case 3"});
        JButton computeButton = new JButton("Get Roommates");
        computeButton.setBackground(BG_COLOR);
        computeButton.setForeground(TXT);
        rmCaseSelector.setBackground(BG_COLOR);
        rmCaseSelector.setForeground(TXT);

        computeButton.addActionListener(e -> {
            int idx = rmCaseSelector.getSelectedIndex();
            List<UniversityStudent> data = testCases.get(idx);
            // clear previous roommates
            data.forEach(s -> s.setRoommate(null));
            GaleShapley.assignRoommates(data);
            StringBuilder sb = new StringBuilder();
            for (UniversityStudent s : data) {
                if (s.getRoommate() != null && s.getName().compareTo(s.getRoommate().getName()) < 0) {
                    sb.append("🏠 Roommate Match: ").append(s.getName())
                            .append("  ⇆  ")
                            .append(s.getRoommate().getName()).append("\n\n");
                }
            }
            roommateArea.setText(sb.toString());
            StudentGraph graph = new StudentGraph(data);
            graphPanel.setGraph(graph, data);
            graphPanel.roomiePath(data);
            tabs = (JTabbedPane) panel.getParent();
            tabs.setSelectedIndex(1);
        });
        JLabel select = new JLabel("Select Data:");
        controls.add(select);
        select.setForeground(TXT);
        controls.add(rmCaseSelector);
        controls.add(computeButton);
        panel.add(controls, BorderLayout.NORTH);
        roommateArea = new JTextArea();
        roommateArea.setEditable(false);
        roommateArea.setBackground(BG_COLOR);
        roommateArea.setForeground(TXT);
        panel.add(new JScrollPane(roommateArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createReferralPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel controls = new JPanel();
        controls.setBackground(BG_COLOR);
        JComboBox<String> refCaseSelector = new JComboBox<>(new String[]{"Test Case 1", "Test Case 2", "Test Case 3"});
        refCaseSelector.setForeground(TXT);
        refCaseSelector.setBackground(BG_COLOR);
        startStudentSelector = new JComboBox<>();
        startStudentSelector.setBackground(BG_COLOR);
        startStudentSelector.setForeground(TXT);
        targetCompanyField = new JTextField(10);
        targetCompanyField.setBackground(BG_COLOR);
        targetCompanyField.setForeground(TXT);
        JButton findButton = new JButton("Find Referral Path");
        findButton.setBackground(BG_COLOR);
        findButton.setForeground(TXT);
        findButton.addActionListener(e -> {
            int idx = refCaseSelector.getSelectedIndex();
            List<UniversityStudent> data = testCases.get(idx);
            String selectedName = (String) startStudentSelector.getSelectedItem();
            UniversityStudent start = data.stream().filter(s -> s.getName().equals(selectedName)).findFirst().orElse(null);
            String target = targetCompanyField.getText().trim();
            if (start != null && !target.isEmpty()) {
                StudentGraph graph = new StudentGraph(data);
                ReferralPathFinder finder = new ReferralPathFinder(graph);
                List<UniversityStudent> path = finder.findReferralPath(start, target);
                StringBuilder sb = new StringBuilder();
                sb.append("🔎 Referral Path to ").append(target).append(":\n\n");

                if (path.isEmpty()) {
                    sb.append("❌ No referral path available.\n");
                } else {
                    for (int i = 0; i < path.size(); i++) {
                        sb.append("️Step ").append(i + 1).append(": ");
                        sb.append(path.get(i).getName());

                        if (i < path.size() - 1)
                            sb.append(" → knows → ").append(path.get(i + 1).getName());

                        sb.append("\n");
                    }

                    sb.append("\n🎯 Best person to contact: ")
                            .append(path.get(path.size() - 1).getName());
                }
                referralArea.setText(sb.toString());
                // Get graph panel paths
                graphPanel.setGraph(graph, data);      // refresh graph with correct data
                graphPanel.refPath(path);
                tabs.setSelectedIndex(1);
            }
        });

        refCaseSelector.addActionListener(e -> {
            int idx = refCaseSelector.getSelectedIndex();
            List<UniversityStudent> data = testCases.get(idx);
            startStudentSelector.removeAllItems();
            data.forEach(s -> startStudentSelector.addItem(s.getName()));
        });
        refCaseSelector.setSelectedIndex(0); // trigger population
        JLabel select = new JLabel("Data:");
        select.setForeground(TXT);
        controls.add(select);
        controls.add(refCaseSelector);
        JLabel start = new JLabel("Start:");
        start.setForeground(TXT);
        controls.add(start);
        controls.add(startStudentSelector);
        JLabel target = new JLabel("Target Company:");
        target.setForeground(TXT);
        controls.add(target);
        controls.add(targetCompanyField);
        controls.add(findButton);
        panel.add(controls, BorderLayout.NORTH);
        referralArea = new JTextArea();
        referralArea.setBackground(BG_COLOR);
        referralArea.setForeground(TXT);
        referralArea.setEditable(false);
        panel.add(new JScrollPane(referralArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFriendsChatPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        JPanel controls = new JPanel();
        controls.setBackground(BG_COLOR);

        // Make dropdown to select which student you want to look at
        JComboBox<String> studentSelection = new JComboBox<>();
        for (List<UniversityStudent> list: testCases){
            for (UniversityStudent student: list) {
                studentSelection.addItem(student.getName());
            }
        }
        studentSelection.setBackground(BG_COLOR);
        studentSelection.setForeground(TXT);

        // Load in student's data
        JButton loadData = new JButton("Load Data");
        loadData.setBackground(BG_COLOR);
        loadData.setForeground(TXT);

        // Action event listener for loading data button
        loadData.addActionListener(e -> {
            String name = (String) studentSelection.getSelectedItem();
            StringBuilder sb = new StringBuilder();

            // Find student and show their friends then chats
            for (List<UniversityStudent> list: testCases){
                for (UniversityStudent student: list) {
                    if (student.getName().equals(name)) {
                        sb.append(student.getName()).append("'s Friends:").append("\n");
                        if (student.getFriends().isEmpty()) {
                            sb.append("None\n");
                        } else {
                            for (UniversityStudent f : student.getFriends()) {
                                sb.append("• ").append(f.getName()).append("\n");
                            }
                        }

                        // Chats
                        sb.append("\nChat History:\n");
                        if (student.getFriends().isEmpty()) {
                            sb.append("None\n");
                        } else {
                            for (UniversityStudent friend : student.getFriends()) {
                                if (friend == null) continue;
                                List<String> chats = student.getChats(friend);
                                if (chats.isEmpty()) { sb.append("None\n"); }
                                else {
                                    //chats.forEach(msg -> sb.append("   • ").append(msg).append("\n"));
                                    for (String chat : chats) {
                                        sb.append(" ").append(chat).append("\n");
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
            chatHistoryArea.setText(sb.toString());
        });
        //Add components to frame
        JLabel select = new JLabel("Select Student:");
        controls.add(select);
        select.setForeground(TXT);
        select.setBackground(BG_COLOR);
        controls.add(studentSelection);
        controls.add(loadData);
        panel.add(controls, BorderLayout.NORTH); // add to main panel area


        chatHistoryArea = new JTextArea();
        chatHistoryArea.setBackground(BG_COLOR);
        chatHistoryArea.setForeground(TXT);
        chatHistoryArea.setEditable(false);
        panel.add(new JScrollPane(chatHistoryArea), BorderLayout.CENTER);


        return panel;
    }

    private void onRunTests() {
        testOutputArea.setText("");
        String sel = (String) testCaseSelector.getSelectedItem();
        if (sel.equals("All Test Cases")) {
            for (int i = 1; i <= testCases.size(); i++) runTests(i);
        } else {
            int num = Integer.parseInt(sel.split(" ")[2]);
            runTests(num);
        }
    }

    private void runTests(int caseNum) {
        testOutputArea.append("=== Test Case " + caseNum + " ===\n");
        List<UniversityStudent> data = testCases.get(caseNum - 1);
        // Print data
        data.forEach(s -> testOutputArea.append(s + "\n"));
        testOutputArea.append("\n");
        int score = Main.gradeLab(data, caseNum);
        testOutputArea.append("Test Case " + caseNum + " Score: " + score + "\n\n");
    }



    // Custom panel to draw the graph
    private static class GraphPanel extends JPanel {
        private Set<UniversityStudent> roommateNodes = new HashSet<>();
        private StudentGraph graph;
        private List<UniversityStudent> nodes;
        private List<UniversityStudent> referralPath;

        void setGraph(StudentGraph g, List<UniversityStudent> data) {
            this.graph = g;
            this.nodes = data;
            // Reset referrals
            this.referralPath = null;
            repaint();
        }

        // Get referralPath to shwo in graph
        void refPath(List<UniversityStudent> path) {
            this.referralPath = path;
            repaint();
        }

        void roomiePath(List<UniversityStudent> path) {
            roommateNodes.clear();
            for (UniversityStudent student: path) {
                if (student.getRoommate() != null) {
                    roommateNodes.add(student);
                    roommateNodes.add(student.getRoommate());
                }
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (graph == null || nodes == null) return;
            int width = getWidth(), height = getHeight();
            int r = Math.min(width, height) / 3;
            int cx = width / 2, cy = height / 2;
            Map<UniversityStudent, Point> coords = new HashMap<>();
            int n = nodes.size();
            for (int i = 0; i < n; i++) {
                double angle = 2 * Math.PI * i / n;
                int x = cx + (int) (r * Math.cos(angle));
                int y = cy + (int) (r * Math.sin(angle));
                coords.put(nodes.get(i), new Point(x, y));
            }
            Graphics2D g2 = (Graphics2D) g;
            // Draw edges
            for (UniversityStudent s : nodes) {
                for (StudentGraph.Edge e : graph.getNeighbors(s)) {
                    UniversityStudent t = e.neighbor;
                    if (nodes.indexOf(t) <= nodes.indexOf(s)) continue; // draw once
                    Point p1 = coords.get(s), p2 = coords.get(t);

                    // What color to make edge depending on relationship?
                    boolean isRoommate = s.getRoommate() != null && s.getRoommate().equals(t);
                    boolean isReferral =false;
                    if (referralPath !=null){
                        for (int k = 0; k < referralPath.size() - 1; k++) {
                            if ((referralPath.get(k).equals(s) && referralPath.get(k + 1).equals(t)) ||
                                    (referralPath.get(k).equals(t) && referralPath.get(k + 1).equals(s))) {
                                isReferral = true;
                                break;
                            }
                        }
                    }

                    if (isReferral) g2.setColor(Color.MAGENTA);
                    else if (isRoommate) { g2.setColor(Color.GREEN); g2.setStroke(new BasicStroke(5)); }
                    else g2.setColor(Color.WHITE);

                    // Draw edge
                    g2.setStroke(new BasicStroke(3));
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);

                    // Weight label
                    g2.setFont(new Font("Arial", Font.BOLD, 13));
                    g2.setColor(Color.BLUE);
                    int mx = (p1.x + p2.x) / 2, my = (p1.y + p2.y) / 2;
                    g2.drawString(String.valueOf(e.weight), mx, my);
                }
            }
            // Draw nodes

            for (UniversityStudent s : nodes) {
                Point p = coords.get(s);

                // MAke roommie nodes glow
                boolean isRoommateNode = roommateNodes.contains(s);
                if (isRoommateNode) {
                    g2.setColor(new Color(0, 255, 0, 80));  // green glow
                    g2.fillOval(p.x - 30, p.y - 30, 60, 60);
                }

                // Background
                g2.setColor(new Color(255, 255, 255, 230));
                g2.fillOval(p.x - 22, p.y - 22, 48, 48);

                //Border
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(3));
                g2.drawOval(p.x - 22, p.y - 22, 48, 48);

                // Put name in middle
                g2.setColor(Color.RED);
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(s.getName());
                g2.drawString(s.getName(),p.x - textWidth / 2, p.y + 4);
            }
            // Legend for colors
            g2.setColor(Color.WHITE);
            g2.drawString("White Edges = normal connection", 10, height - 45);
            g2.setColor(Color.GREEN);
            g2.drawString("Green Edges/Highlight = Roommates (only connected by edge if a weighted connection exists)", 10, height - 30);
            g2.setColor(Color.MAGENTA);
            g2.drawString("Magenta Edges = Internship Referral Path", 10, height - 15);
        }
    }

    private List<UniversityStudent> socialConnections(List<UniversityStudent> students) {

        ExecutorService exec = Executors.newFixedThreadPool(4);

        if (students.size() >= 2) {
            UniversityStudent s1 = students.get(0);
            UniversityStudent s2 = students.get(1);
            // Friends
            exec.submit(new FriendRequestThread(s1, s2));
            exec.submit(new FriendRequestThread(s2, s1));

            // Chats
            exec.submit(new ChatThread(s1, s2, "Hello from " + s1.getName()));
            exec.submit(new ChatThread(s2, s1, "Reply from  " + s2.getName()));
        }

        // Add optional extra friendships / chats
        for (int i = 0; i + 1 < students.size(); i++) {
            UniversityStudent a = students.get(i);
            UniversityStudent b = students.get(i + 1);

            exec.submit(new FriendRequestThread(a, b));
            exec.submit(new ChatThread(a, b, a.getName() + " → Hey!! How are you " + b.getName() ));
            exec.submit(new ChatThread(b, a, b.getName() + " → I'm gr8, how r u  " + a.getName() + "?"));
        }

        exec.shutdown();
        try {
            exec.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {}

        return students;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NetworkLabUI().setVisible(true));
    }
}

