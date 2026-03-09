/*    */ package net.minecraft.world.item.equipment.trim;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ExtraCodecs;
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
/*    */   private final String suffix;
/*    */   
/* 60 */   public String suffix() { return this.suffix; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/equipment/trim/MaterialAssetGroup$AssetInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #60	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/equipment/trim/MaterialAssetGroup$AssetInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 61 */   public static final Codec<AssetInfo> CODEC = ExtraCodecs.RESOURCE_PATH_CODEC.xmap(AssetInfo::new, AssetInfo::suffix); public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/equipment/trim/MaterialAssetGroup$AssetInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #60	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/trim/MaterialAssetGroup$AssetInfo; }
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/equipment/trim/MaterialAssetGroup$AssetInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #60	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/trim/MaterialAssetGroup$AssetInfo; }
/* 63 */   public static final StreamCodec<ByteBuf, AssetInfo> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(AssetInfo::new, AssetInfo::suffix);
/*    */   
/*    */   public AssetInfo(String suffix) {
/* 66 */     if (!Identifier.isValidPath(suffix))
/* 67 */       throw new IllegalArgumentException("Invalid string to use as a resource path element: " + suffix); 
/*    */     this.suffix = suffix;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\equipment\trim\MaterialAssetGroup$AssetInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */