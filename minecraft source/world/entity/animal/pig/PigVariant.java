/*    */ package net.minecraft.world.entity.animal.pig;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.entity.variant.ModelAndTexture;
/*    */ import net.minecraft.world.entity.variant.PriorityProvider;
/*    */ import net.minecraft.world.entity.variant.SpawnCondition;
/*    */ import net.minecraft.world.entity.variant.SpawnContext;
/*    */ import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
/*    */ 
/*    */ public final class PigVariant extends Record implements PriorityProvider<SpawnContext, SpawnCondition> {
/*    */   private final ModelAndTexture<ModelType> modelAndTexture;
/*    */   private final SpawnPrioritySelectors spawnConditions;
/*    */   
/* 20 */   public PigVariant(ModelAndTexture<ModelType> modelAndTexture, SpawnPrioritySelectors spawnConditions) { this.modelAndTexture = modelAndTexture; this.spawnConditions = spawnConditions; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/pig/PigVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 20 */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/pig/PigVariant; } public ModelAndTexture<ModelType> modelAndTexture() { return this.modelAndTexture; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/pig/PigVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/pig/PigVariant; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/pig/PigVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/pig/PigVariant;
/* 20 */     //   0	8	1	o	Ljava/lang/Object; } public SpawnPrioritySelectors spawnConditions() { return this.spawnConditions; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static final Codec<PigVariant> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(
/* 26 */         ModelAndTexture.codec(ModelType.CODEC, ModelType.NORMAL).forGetter(PigVariant::modelAndTexture), SpawnPrioritySelectors.CODEC
/* 27 */         .fieldOf("spawn_conditions").forGetter(PigVariant::spawnConditions))
/* 28 */       .apply(i, PigVariant::new));
/*    */ 
/*    */   
/* 31 */   public static final Codec<PigVariant> NETWORK_CODEC = RecordCodecBuilder.create(i -> i.group(
/* 32 */         ModelAndTexture.codec(ModelType.CODEC, ModelType.NORMAL).forGetter(PigVariant::modelAndTexture))
/* 33 */       .apply(i, PigVariant::new));
/*    */   
/* 35 */   public static final Codec<Holder<PigVariant>> CODEC = RegistryFixedCodec.create(Registries.PIG_VARIANT);
/* 36 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<PigVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.PIG_VARIANT);
/*    */ 
/*    */   
/* 39 */   private PigVariant(ModelAndTexture<ModelType> assetInfo) { this(assetInfo, SpawnPrioritySelectors.EMPTY); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   public List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() { return this.spawnConditions.selectors(); }
/*    */   
/*    */   public enum ModelType
/*    */     implements StringRepresentable {
/* 48 */     NORMAL("normal"),
/* 49 */     COLD("cold"); public static final Codec<ModelType> CODEC; private final String name;
/*    */     
/*    */     static  {
/* 52 */       CODEC = StringRepresentable.fromEnum(ModelType::values);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 57 */     ModelType(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 62 */     public String getSerializedName() { return this.name; }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\pig\PigVariant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */