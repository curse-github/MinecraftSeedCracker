/*    */ package net.minecraft.network.protocol.common.custom;
/*    */ 
/*    */ public final class BrandPayload extends Record implements CustomPacketPayload {
/*    */   private final String brand;
/*    */   
/*  6 */   public BrandPayload(String brand) { this.brand = brand; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/custom/BrandPayload;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  6 */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/BrandPayload; } public String brand() { return this.brand; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/custom/BrandPayload;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/BrandPayload; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/custom/BrandPayload;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/custom/BrandPayload;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*  7 */   public static final StreamCodec<FriendlyByteBuf, BrandPayload> STREAM_CODEC = CustomPacketPayload.codec(BrandPayload::write, BrandPayload::new);
/*  8 */   public static final CustomPacketPayload.Type<BrandPayload> TYPE = CustomPacketPayload.createType("brand");
/*    */ 
/*    */   
/* 11 */   private BrandPayload(FriendlyByteBuf input) { this(input.readUtf()); }
/*    */ 
/*    */ 
/*    */   
/* 15 */   private void write(FriendlyByteBuf output) { output.writeUtf(this.brand); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public CustomPacketPayload.Type<BrandPayload> type() { return TYPE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\custom\BrandPayload.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */