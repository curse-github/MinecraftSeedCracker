/*    */ package net.minecraft.world.entity.animal.nautilus;
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
/*    */ public final class ZombieNautilusVariant extends Record implements PriorityProvider<SpawnContext, SpawnCondition> {
/*    */   private final ModelAndTexture<ModelType> modelAndTexture;
/*    */   private final SpawnPrioritySelectors spawnConditions;
/*    */   
/* 20 */   public ZombieNautilusVariant(ModelAndTexture<ModelType> modelAndTexture, SpawnPrioritySelectors spawnConditions) { this.modelAndTexture = modelAndTexture; this.spawnConditions = spawnConditions; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/nautilus/ZombieNautilusVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 20 */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/nautilus/ZombieNautilusVariant; } public ModelAndTexture<ModelType> modelAndTexture() { return this.modelAndTexture; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/nautilus/ZombieNautilusVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/nautilus/ZombieNautilusVariant; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/nautilus/ZombieNautilusVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/nautilus/ZombieNautilusVariant;
/* 20 */     //   0	8	1	o	Ljava/lang/Object; } public SpawnPrioritySelectors spawnConditions() { return this.spawnConditions; }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static final Codec<ZombieNautilusVariant> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(
/* 25 */         ModelAndTexture.codec(ModelType.CODEC, ModelType.NORMAL).forGetter(ZombieNautilusVariant::modelAndTexture), SpawnPrioritySelectors.CODEC
/* 26 */         .fieldOf("spawn_conditions").forGetter(ZombieNautilusVariant::spawnConditions))
/* 27 */       .apply(i, ZombieNautilusVariant::new));
/*    */   
/* 29 */   public static final Codec<ZombieNautilusVariant> NETWORK_CODEC = RecordCodecBuilder.create(i -> i.group(
/* 30 */         ModelAndTexture.codec(ModelType.CODEC, ModelType.NORMAL).forGetter(ZombieNautilusVariant::modelAndTexture))
/* 31 */       .apply(i, ZombieNautilusVariant::new));
/*    */   
/* 33 */   public static final Codec<Holder<ZombieNautilusVariant>> CODEC = RegistryFixedCodec.create(Registries.ZOMBIE_NAUTILUS_VARIANT);
/* 34 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<ZombieNautilusVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.ZOMBIE_NAUTILUS_VARIANT);
/*    */ 
/*    */   
/* 37 */   private ZombieNautilusVariant(ModelAndTexture<ModelType> assetInfo) { this(assetInfo, SpawnPrioritySelectors.EMPTY); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() { return this.spawnConditions.selectors(); }
/*    */   
/*    */   public enum ModelType
/*    */     implements StringRepresentable {
/* 46 */     NORMAL("normal"),
/* 47 */     WARM("warm"); public static final Codec<ModelType> CODEC; private final String name;
/*    */     static  {
/* 49 */       CODEC = StringRepresentable.fromEnum(ModelType::values);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 54 */     ModelType(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 59 */     public String getSerializedName() { return this.name; }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\nautilus\ZombieNautilusVariant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */