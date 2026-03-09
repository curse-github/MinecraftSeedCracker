/*    */ package net.minecraft.network;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public final class DisconnectionDetails extends Record {
/*    */   private final Component reason;
/*    */   private final Optional<Path> report;
/*    */   private final Optional<URI> bugReportLink;
/*    */   
/*  9 */   public Optional<URI> bugReportLink() { return this.bugReportLink; } public Optional<Path> report() { return this.report; } public Component reason() { return this.reason; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/DisconnectionDetails;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/DisconnectionDetails;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public DisconnectionDetails(Component reason, Optional<Path> report, Optional<URI> bugReportLink) { this.reason = reason; this.report = report; this.bugReportLink = bugReportLink; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/DisconnectionDetails;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/DisconnectionDetails; }
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/DisconnectionDetails;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/DisconnectionDetails; }
/* 11 */   public DisconnectionDetails(Component reason) { this(reason, Optional.empty(), Optional.empty()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\DisconnectionDetails.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */