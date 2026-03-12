/*    */ package net.minecraft.world.entity.animal.feline;
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
/*    */ public final class CatVariant extends Record implements PriorityProvider<SpawnContext, SpawnCondition> {
/*    */   private final ClientAsset.ResourceTexture assetInfo;
/*    */   private final SpawnPrioritySelectors spawnConditions;
/*    */   
/* 19 */   public CatVariant(ClientAsset.ResourceTexture assetInfo, SpawnPrioritySelectors spawnConditions) { this.assetInfo = assetInfo; this.spawnConditions = spawnConditions; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/feline/CatVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 19 */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/feline/CatVariant; } public ClientAsset.ResourceTexture assetInfo() { return this.assetInfo; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/feline/CatVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/feline/CatVariant; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/feline/CatVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/feline/CatVariant;
/* 19 */     //   0	8	1	o	Ljava/lang/Object; } public SpawnPrioritySelectors spawnConditions() { return this.spawnConditions; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static final Codec<CatVariant> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(ClientAsset.ResourceTexture.DEFAULT_FIELD_CODEC
/* 25 */         .forGetter(CatVariant::assetInfo), SpawnPrioritySelectors.CODEC
/* 26 */         .fieldOf("spawn_conditions").forGetter(CatVariant::spawnConditions))
/* 27 */       .apply(i, CatVariant::new));
/*    */   
/* 29 */   public static final Codec<CatVariant> NETWORK_CODEC = RecordCodecBuilder.create(i -> i.group(ClientAsset.ResourceTexture.DEFAULT_FIELD_CODEC
/* 30 */         .forGetter(CatVariant::assetInfo))
/* 31 */       .apply(i, CatVariant::new));
/*    */   
/* 33 */   public static final Codec<Holder<CatVariant>> CODEC = RegistryFixedCodec.create(Registries.CAT_VARIANT);
/* 34 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<CatVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.CAT_VARIANT);
/*    */ 
/*    */   
/* 37 */   private CatVariant(ClientAsset.ResourceTexture assetInfo) { this(assetInfo, SpawnPrioritySelectors.EMPTY); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() { return this.spawnConditions.selectors(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\feline\CatVariant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */