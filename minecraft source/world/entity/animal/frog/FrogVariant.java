/*    */ package net.minecraft.world.entity.animal.frog;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.ClientAsset;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.entity.variant.PriorityProvider;
/*    */ import net.minecraft.world.entity.variant.SpawnCondition;
/*    */ import net.minecraft.world.entity.variant.SpawnContext;
/*    */ import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
/*    */ 
/*    */ public final class FrogVariant extends Record implements PriorityProvider<SpawnContext, SpawnCondition> {
/*    */   private final ClientAsset.ResourceTexture assetInfo;
/*    */   private final SpawnPrioritySelectors spawnConditions;
/*    */   
/* 19 */   public FrogVariant(ClientAsset.ResourceTexture assetInfo, SpawnPrioritySelectors spawnConditions) { this.assetInfo = assetInfo; this.spawnConditions = spawnConditions; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/frog/FrogVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 19 */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/frog/FrogVariant; } public ClientAsset.ResourceTexture assetInfo() { return this.assetInfo; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/frog/FrogVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/frog/FrogVariant; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/frog/FrogVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/frog/FrogVariant;
/* 19 */     //   0	8	1	o	Ljava/lang/Object; } public SpawnPrioritySelectors spawnConditions() { return this.spawnConditions; }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static final Codec<FrogVariant> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(ClientAsset.ResourceTexture.DEFAULT_FIELD_CODEC
/* 24 */         .forGetter(FrogVariant::assetInfo), SpawnPrioritySelectors.CODEC
/* 25 */         .fieldOf("spawn_conditions").forGetter(FrogVariant::spawnConditions))
/* 26 */       .apply(i, FrogVariant::new));
/*    */ 
/*    */   
/* 29 */   public static final Codec<FrogVariant> NETWORK_CODEC = RecordCodecBuilder.create(i -> i.group(ClientAsset.ResourceTexture.DEFAULT_FIELD_CODEC
/* 30 */         .forGetter(FrogVariant::assetInfo))
/* 31 */       .apply(i, FrogVariant::new));
/*    */   
/* 33 */   public static final Codec<Holder<FrogVariant>> CODEC = RegistryFixedCodec.create(Registries.FROG_VARIANT);
/* 34 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<FrogVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.FROG_VARIANT);
/*    */ 
/*    */   
/* 37 */   private FrogVariant(ClientAsset.ResourceTexture assetInfo) { this(assetInfo, SpawnPrioritySelectors.EMPTY); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() { return this.spawnConditions.selectors(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\frog\FrogVariant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */