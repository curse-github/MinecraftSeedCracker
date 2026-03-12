/*     */ package net.minecraft.server.gui;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogQueues;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.FocusAdapter;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.util.Collection;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollBar;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.EtchedBorder;
/*     */ import javax.swing.border.TitledBorder;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import net.minecraft.DefaultUncaughtExceptionHandler;
/*     */ import net.minecraft.server.dedicated.DedicatedServer;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class MinecraftServerGui extends JComponent {
/*  36 */   private static final Font MONOSPACED = new Font("Monospaced", 0, 12);
/*  37 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String TITLE = "Minecraft server";
/*     */   private static final String SHUTDOWN_TITLE = "Minecraft server - shutting down!";
/*     */   private final DedicatedServer server;
/*     */   private Thread logAppenderThread;
/*     */   private final Collection<Runnable> finalizers;
/*     */   private final AtomicBoolean isClosing;
/*     */   
/*     */   public static MinecraftServerGui showFrameFor(final DedicatedServer server) {
/*     */     try {
/*  48 */       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
/*  49 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/*  52 */     final JFrame frame = new JFrame("Minecraft server");
/*  53 */     final MinecraftServerGui gui = new MinecraftServerGui(server);
/*  54 */     frame.setDefaultCloseOperation(2);
/*  55 */     frame.add(gui);
/*  56 */     frame.pack();
/*  57 */     frame.setLocationRelativeTo(null);
/*  58 */     frame.setVisible(true);
/*  59 */     frame.addWindowListener(new WindowAdapter()
/*     */         {
/*     */           public void windowClosing(WindowEvent event) {
/*  62 */             if (!this.val$gui.isClosing.getAndSet(true)) {
/*  63 */               frame.setTitle("Minecraft server - shutting down!");
/*  64 */               server.halt(true);
/*  65 */               gui.runFinalizers();
/*     */             } 
/*     */           }
/*     */         });
/*  69 */     Objects.requireNonNull(frame); gui.addFinalizer(frame::dispose);
/*  70 */     gui.start();
/*  71 */     return gui;
/*     */   } private MinecraftServerGui(DedicatedServer server) {
/*     */     this.finalizers = Lists.newArrayList();
/*     */     this.isClosing = new AtomicBoolean();
/*  75 */     this.server = server;
/*  76 */     setPreferredSize(new Dimension(854, 480));
/*     */     
/*  78 */     setLayout(new BorderLayout());
/*     */     try {
/*  80 */       add(buildChatPanel(), "Center");
/*  81 */       add(buildInfoPanel(), "West");
/*  82 */     } catch (Exception e) {
/*  83 */       LOGGER.error("Couldn't build server GUI", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  88 */   public void addFinalizer(Runnable finalizer) { this.finalizers.add(finalizer); }
/*     */ 
/*     */   
/*     */   private JComponent buildInfoPanel() {
/*  92 */     JPanel panel = new JPanel(new BorderLayout());
/*  93 */     StatsComponent comp = new StatsComponent(this.server);
/*  94 */     Objects.requireNonNull(comp); this.finalizers.add(comp::close);
/*  95 */     panel.add(comp, "North");
/*  96 */     panel.add(buildPlayerPanel(), "Center");
/*  97 */     panel.setBorder(new TitledBorder(new EtchedBorder(), "Stats"));
/*  98 */     return panel;
/*     */   }
/*     */   
/*     */   private JComponent buildPlayerPanel() {
/* 102 */     JList<?> playerList = new PlayerListComponent<?>(this.server);
/* 103 */     JScrollPane scrollPane = new JScrollPane(playerList, 22, 30);
/* 104 */     scrollPane.setBorder(new TitledBorder(new EtchedBorder(), "Players"));
/*     */     
/* 106 */     return scrollPane;
/*     */   }
/*     */   
/*     */   private JComponent buildChatPanel() {
/* 110 */     JPanel panel = new JPanel(new BorderLayout());
/* 111 */     JTextArea chatArea = new JTextArea();
/* 112 */     JScrollPane scrollPane = new JScrollPane(chatArea, 22, 30);
/* 113 */     chatArea.setEditable(false);
/* 114 */     chatArea.setFont(MONOSPACED);
/*     */     
/* 116 */     JTextField chatField = new JTextField();
/* 117 */     chatField.addActionListener(event -> {
/* 118 */           String text = chatField.getText().trim();
/* 119 */           if (!text.isEmpty()) {
/* 120 */             this.server.handleConsoleInput(text, this.server.createCommandSourceStack());
/*     */           }
/* 122 */           chatField.setText("");
/*     */         });
/*     */     
/* 125 */     chatArea.addFocusListener(new FocusAdapter(this)
/*     */         {
/*     */           public void focusGained(FocusEvent arg0) {}
/*     */         });
/*     */ 
/*     */     
/* 131 */     panel.add(scrollPane, "Center");
/* 132 */     panel.add(chatField, "South");
/* 133 */     panel.setBorder(new TitledBorder(new EtchedBorder(), "Log and chat"));
/*     */     
/* 135 */     this.logAppenderThread = new Thread(() -> {
/*     */           String line;
/* 137 */           while ((line = LogQueues.getNextLogEvent("ServerGuiConsole")) != null) {
/* 138 */             print(chatArea, scrollPane, line);
/*     */           }
/*     */         });
/* 141 */     this.logAppenderThread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER));
/* 142 */     this.logAppenderThread.setDaemon(true);
/* 143 */     return panel;
/*     */   }
/*     */ 
/*     */   
/* 147 */   public void start() { this.logAppenderThread.start(); }
/*     */ 
/*     */   
/*     */   public void close() {
/* 151 */     if (!this.isClosing.getAndSet(true)) {
/* 152 */       runFinalizers();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 157 */   private void runFinalizers() { this.finalizers.forEach(Runnable::run); }
/*     */ 
/*     */   
/*     */   public void print(JTextArea console, JScrollPane scrollPane, String line) {
/* 161 */     if (!SwingUtilities.isEventDispatchThread()) {
/* 162 */       SwingUtilities.invokeLater(() -> print(console, scrollPane, line));
/*     */       
/*     */       return;
/*     */     } 
/* 166 */     Document document = console.getDocument();
/* 167 */     JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
/* 168 */     boolean shouldScroll = false;
/*     */     
/* 170 */     if (scrollPane.getViewport().getView() == console) {
/* 171 */       shouldScroll = (scrollBar.getValue() + scrollBar.getSize().getHeight() + (MONOSPACED.getSize() * 4) > scrollBar.getMaximum());
/*     */     }
/*     */     
/*     */     try {
/* 175 */       document.insertString(document.getLength(), line, null);
/* 176 */     } catch (BadLocationException badLocationException) {}
/*     */ 
/*     */     
/* 179 */     if (shouldScroll)
/* 180 */       scrollBar.setValue(2147483647); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\gui\MinecraftServerGui.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */