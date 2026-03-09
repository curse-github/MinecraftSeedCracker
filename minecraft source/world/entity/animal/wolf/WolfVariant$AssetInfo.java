/*    */ package net.minecraft.world.entity.animal.wolf;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.ClientAsset;
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
/*    */ public final class AssetInfo
/*    */   extends Record
/*    */ {
/*    */   private final ClientAsset.ResourceTexture wild;
/*    */   private final ClientAsset.ResourceTexture tame;
/*    */   private final ClientAsset.ResourceTexture angry;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 45 */   public AssetInfo(ClientAsset.ResourceTexture wild, ClientAsset.ResourceTexture tame, ClientAsset.ResourceTexture angry) { this.wild = wild; this.tame = tame; this.angry = angry; } public ClientAsset.ResourceTexture wild() { return this.wild; } public ClientAsset.ResourceTexture tame() { return this.tame; } public ClientAsset.ResourceTexture angry() { return this.angry; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public static final Codec<AssetInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(ClientAsset.ResourceTexture.CODEC
/* 51 */         .fieldOf("wild").forGetter(AssetInfo::wild), ClientAsset.ResourceTexture.CODEC
/* 52 */         .fieldOf("tame").forGetter(AssetInfo::tame), ClientAsset.ResourceTexture.CODEC
/* 53 */         .fieldOf("angry").forGetter(AssetInfo::angry))
/* 54 */       .apply(instance, AssetInfo::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\wolf\WolfVariant$AssetInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */