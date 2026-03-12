/*    */ package net.minecraft.server.gui;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.text.DecimalFormat;
/*    */ import java.text.DecimalFormatSymbols;
/*    */ import java.util.Locale;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.Timer;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.TimeUtil;
/*    */ 
/*    */ public class StatsComponent
/*    */   extends JComponent {
/* 17 */   private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("########0.000", DecimalFormatSymbols.getInstance(Locale.ROOT)); private final int[] values; private int vp;
/*    */   public StatsComponent(MinecraftServer server) {
/* 19 */     this.values = new int[256];
/*    */     
/* 21 */     this.msgs = new String[11];
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 26 */     this.server = server;
/* 27 */     setPreferredSize(new Dimension(456, 246));
/* 28 */     setMinimumSize(new Dimension(456, 246));
/* 29 */     setMaximumSize(new Dimension(456, 246));
/* 30 */     this.timer = new Timer(500, event -> tick());
/* 31 */     this.timer.start();
/* 32 */     setBackground(Color.BLACK);
/*    */   }
/*    */   private final String[] msgs; private final MinecraftServer server; private final Timer timer;
/*    */   private void tick() {
/* 36 */     long usedRam = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
/* 37 */     this.msgs[0] = "Memory use: " + usedRam / 1024L / 1024L + " mb (" + Runtime.getRuntime().freeMemory() * 100L / Runtime.getRuntime().maxMemory() + "% free)";
/* 38 */     this.msgs[1] = "Avg tick: " + DECIMAL_FORMAT.format(this.server.getAverageTickTimeNanos() / TimeUtil.NANOSECONDS_PER_MILLISECOND) + " ms";
/* 39 */     this.values[this.vp++ & 0xFF] = (int)(usedRam * 100L / Runtime.getRuntime().maxMemory());
/* 40 */     repaint();
/*    */   }
/*    */ 
/*    */   
/*    */   public void paint(Graphics g) {
/* 45 */     g.setColor(new Color(16777215));
/* 46 */     g.fillRect(0, 0, 456, 246);
/*    */     
/* 48 */     for (int x = 0; x < 256; x++) {
/* 49 */       int v = this.values[x + this.vp & 0xFF];
/* 50 */       g.setColor(new Color(v + 28 << 16));
/* 51 */       g.fillRect(x, 100 - v, 1, v);
/*    */     } 
/* 53 */     g.setColor(Color.BLACK);
/* 54 */     for (int i = 0; i < this.msgs.length; i++) {
/* 55 */       String msg = this.msgs[i];
/* 56 */       if (msg != null) {
/* 57 */         g.drawString(msg, 32, 116 + i * 16);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 63 */   public void close() { this.timer.stop(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\gui\StatsComponent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */