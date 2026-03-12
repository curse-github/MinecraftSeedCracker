/*    */ package net.minecraft.server.level;
/*    */ public final class ClientInformation extends Record {
/*    */   private final String language;
/*    */   private final int viewDistance;
/*    */   private final ChatVisiblity chatVisibility;
/*    */   private final boolean chatColors;
/*    */   private final int modelCustomisation;
/*    */   
/*  9 */   public ClientInformation(String language, int viewDistance, ChatVisiblity chatVisibility, boolean chatColors, int modelCustomisation, HumanoidArm mainHand, boolean textFilteringEnabled, boolean allowsListing, ParticleStatus particleStatus) { this.language = language; this.viewDistance = viewDistance; this.chatVisibility = chatVisibility; this.chatColors = chatColors; this.modelCustomisation = modelCustomisation; this.mainHand = mainHand; this.textFilteringEnabled = textFilteringEnabled; this.allowsListing = allowsListing; this.particleStatus = particleStatus; } private final HumanoidArm mainHand; private final boolean textFilteringEnabled; private final boolean allowsListing; private final ParticleStatus particleStatus; public static final int MAX_LANGUAGE_LENGTH = 16; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ClientInformation;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/level/ClientInformation; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ClientInformation;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/level/ClientInformation; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ClientInformation;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/level/ClientInformation;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public String language() { return this.language; } public int viewDistance() { return this.viewDistance; } public ChatVisiblity chatVisibility() { return this.chatVisibility; } public boolean chatColors() { return this.chatColors; } public int modelCustomisation() { return this.modelCustomisation; } public HumanoidArm mainHand() { return this.mainHand; } public boolean textFilteringEnabled() { return this.textFilteringEnabled; } public boolean allowsListing() { return this.allowsListing; } public ParticleStatus particleStatus() { return this.particleStatus; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientInformation(FriendlyByteBuf input) {
/* 24 */     this(input
/* 25 */         .readUtf(16), input
/* 26 */         .readByte(), (ChatVisiblity)input
/* 27 */         .readEnum(ChatVisiblity.class), input
/* 28 */         .readBoolean(), input
/* 29 */         .readUnsignedByte(), (HumanoidArm)input
/* 30 */         .readEnum(HumanoidArm.class), input
/* 31 */         .readBoolean(), input
/* 32 */         .readBoolean(), (ParticleStatus)input
/* 33 */         .readEnum(ParticleStatus.class));
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf output) {
/* 38 */     output.writeUtf(this.language);
/* 39 */     output.writeByte(this.viewDistance);
/* 40 */     output.writeEnum(this.chatVisibility);
/* 41 */     output.writeBoolean(this.chatColors);
/* 42 */     output.writeByte(this.modelCustomisation);
/* 43 */     output.writeEnum(this.mainHand);
/* 44 */     output.writeBoolean(this.textFilteringEnabled);
/* 45 */     output.writeBoolean(this.allowsListing);
/* 46 */     output.writeEnum(this.particleStatus);
/*    */   }
/*    */ 
/*    */   
/* 50 */   public static ClientInformation createDefault() { return new ClientInformation("en_us", 2, ChatVisiblity.FULL, true, 0, Player.DEFAULT_MAIN_HAND, false, false, ParticleStatus.ALL); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ClientInformation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */