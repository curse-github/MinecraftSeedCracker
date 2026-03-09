/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.commands.execution.TraceCallbacks;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Tracer
/*     */   implements CommandSource, TraceCallbacks
/*     */ {
/*     */   public static final int INDENT_OFFSET = 1;
/*     */   private final PrintWriter output;
/*     */   private int lastIndent;
/*     */   private boolean waitingForResult;
/*     */   
/* 164 */   private Tracer(PrintWriter output) { this.output = output; }
/*     */ 
/*     */   
/*     */   private void indentAndSave(int value) {
/* 168 */     printIndent(value);
/* 169 */     this.lastIndent = value;
/*     */   }
/*     */   
/*     */   private void printIndent(int value) {
/* 173 */     for (int i = 0; i < value + 1; i++) {
/* 174 */       this.output.write("    ");
/*     */     }
/*     */   }
/*     */   
/*     */   private void newLine() {
/* 179 */     if (this.waitingForResult) {
/* 180 */       this.output.println();
/* 181 */       this.waitingForResult = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCommand(int depth, String command) {
/* 187 */     newLine();
/* 188 */     indentAndSave(depth);
/* 189 */     this.output.print("[C] ");
/* 190 */     this.output.print(command);
/* 191 */     this.waitingForResult = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onReturn(int depth, String command, int result) {
/* 196 */     if (this.waitingForResult) {
/* 197 */       this.output.print(" -> ");
/* 198 */       this.output.println(result);
/* 199 */       this.waitingForResult = false;
/*     */     } else {
/* 201 */       indentAndSave(depth);
/* 202 */       this.output.print("[R = ");
/* 203 */       this.output.print(result);
/* 204 */       this.output.print("] ");
/* 205 */       this.output.println(command);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCall(int depth, Identifier function, int size) {
/* 211 */     newLine();
/* 212 */     indentAndSave(depth);
/* 213 */     this.output.print("[F] ");
/* 214 */     this.output.print(function);
/* 215 */     this.output.print(" size=");
/* 216 */     this.output.println(size);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(String message) {
/* 221 */     newLine();
/* 222 */     indentAndSave(this.lastIndent + 1);
/* 223 */     this.output.print("[E] ");
/* 224 */     this.output.print(message);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendSystemMessage(Component message) {
/* 229 */     newLine();
/* 230 */     printIndent(this.lastIndent + 1);
/* 231 */     this.output.print("[M] ");
/* 232 */     this.output.println(message.getString());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 237 */   public boolean acceptsSuccess() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 242 */   public boolean acceptsFailure() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 247 */   public boolean shouldInformAdmins() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 252 */   public boolean alwaysAccepts() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 257 */   public void close() { IOUtils.closeQuietly(this.output); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\DebugCommand$Tracer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */