/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.level.block.Block;
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
/*    */ public final class Rule
/*    */   extends Record
/*    */ {
/*    */   private final HolderSet<Block> blocks;
/*    */   private final Optional<Float> speed;
/*    */   private final Optional<Boolean> correctForDrops;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/Tool$Rule;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #57	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/Tool$Rule; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/Tool$Rule;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #57	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/Tool$Rule; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/Tool$Rule;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #57	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/Tool$Rule;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 57 */   public Rule(HolderSet<Block> blocks, Optional<Float> speed, Optional<Boolean> correctForDrops) { this.blocks = blocks; this.speed = speed; this.correctForDrops = correctForDrops; } public HolderSet<Block> blocks() { return this.blocks; } public Optional<Float> speed() { return this.speed; } public Optional<Boolean> correctForDrops() { return this.correctForDrops; }
/* 58 */   public static final Codec<Rule> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 59 */         RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(Rule::blocks), ExtraCodecs.POSITIVE_FLOAT
/* 60 */         .optionalFieldOf("speed").forGetter(Rule::speed), Codec.BOOL
/* 61 */         .optionalFieldOf("correct_for_drops").forGetter(Rule::correctForDrops))
/* 62 */       .apply(i, Rule::new));
/*    */   
/* 64 */   public static final StreamCodec<RegistryFriendlyByteBuf, Rule> STREAM_CODEC = StreamCodec.composite(
/* 65 */       ByteBufCodecs.holderSet(Registries.BLOCK), Rule::blocks, ByteBufCodecs.FLOAT
/* 66 */       .apply(ByteBufCodecs::optional), Rule::speed, ByteBufCodecs.BOOL
/* 67 */       .apply(ByteBufCodecs::optional), Rule::correctForDrops, Rule::new);
/*    */ 
/*    */ 
/*    */   
/*    */   public static Rule minesAndDrops(HolderSet<Block> blocks, float speed) {
/* 72 */     return new Rule(blocks, 
/*    */         
/* 74 */         Optional.of(Float.valueOf(speed)), 
/* 75 */         Optional.of(Boolean.valueOf(true)));
/*    */   }
/*    */ 
/*    */   
/*    */   public static Rule deniesDrops(HolderSet<Block> blocks) {
/* 80 */     return new Rule(blocks, 
/*    */         
/* 82 */         Optional.empty(), 
/* 83 */         Optional.of(Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   public static Rule overrideSpeed(HolderSet<Block> blocks, float speed) {
/* 88 */     return new Rule(blocks, 
/*    */         
/* 90 */         Optional.of(Float.valueOf(speed)), 
/* 91 */         Optional.empty());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\Tool$Rule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */