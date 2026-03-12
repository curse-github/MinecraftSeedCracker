/*    */ package net.minecraft.world.item.component;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.damagesource.DamageType;
/*    */ 
/*    */ public final class DamageResistant extends Record {
/* 12 */   public DamageResistant(TagKey<DamageType> types) { this.types = types; } private final TagKey<DamageType> types; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/DamageResistant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/item/component/DamageResistant; } public TagKey<DamageType> types() { return this.types; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/DamageResistant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/DamageResistant; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/DamageResistant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/DamageResistant;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 16 */   public static final Codec<DamageResistant> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 17 */         TagKey.hashedCodec(Registries.DAMAGE_TYPE).fieldOf("types").forGetter(DamageResistant::types))
/* 18 */       .apply(i, DamageResistant::new));
/*    */   
/* 20 */   public static final StreamCodec<RegistryFriendlyByteBuf, DamageResistant> STREAM_CODEC = StreamCodec.composite(
/* 21 */       TagKey.streamCodec(Registries.DAMAGE_TYPE), DamageResistant::types, DamageResistant::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public boolean isResistantTo(DamageSource source) { return source.is(this.types); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\DamageResistant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */