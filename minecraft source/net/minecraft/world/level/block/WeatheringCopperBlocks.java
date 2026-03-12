/*    */ package net.minecraft.world.level.block;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public final class WeatheringCopperBlocks extends Record {
/*    */   private final Block unaffected;
/*    */   private final Block exposed;
/*    */   private final Block weathered;
/*    */   private final Block oxidized;
/*    */   
/* 12 */   public WeatheringCopperBlocks(Block unaffected, Block exposed, Block weathered, Block oxidized, Block waxed, Block waxedExposed, Block waxedWeathered, Block waxedOxidized) { this.unaffected = unaffected; this.exposed = exposed; this.weathered = weathered; this.oxidized = oxidized; this.waxed = waxed; this.waxedExposed = waxedExposed; this.waxedWeathered = waxedWeathered; this.waxedOxidized = waxedOxidized; } private final Block waxed; private final Block waxedExposed; private final Block waxedWeathered; private final Block waxedOxidized; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/WeatheringCopperBlocks;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/WeatheringCopperBlocks; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/WeatheringCopperBlocks;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/WeatheringCopperBlocks; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/WeatheringCopperBlocks;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/block/WeatheringCopperBlocks;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public Block unaffected() { return this.unaffected; } public Block exposed() { return this.exposed; } public Block weathered() { return this.weathered; } public Block oxidized() { return this.oxidized; } public Block waxed() { return this.waxed; } public Block waxedExposed() { return this.waxedExposed; } public Block waxedWeathered() { return this.waxedWeathered; } public Block waxedOxidized() { return this.waxedOxidized; }
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
/*    */   public static <WaxedBlock extends Block, WeatheringBlock extends Block & WeatheringCopper> WeatheringCopperBlocks create(String id, TriFunction<String, Function<BlockBehaviour.Properties, Block>, BlockBehaviour.Properties, Block> register, Function<BlockBehaviour.Properties, WaxedBlock> waxedBlockFactory, BiFunction<WeatheringCopper.WeatherState, BlockBehaviour.Properties, WeatheringBlock> weatheringFactory, Function<WeatheringCopper.WeatherState, BlockBehaviour.Properties> propertiesSupplier) {
/* 34 */     Objects.requireNonNull(waxedBlockFactory);
/* 35 */     Objects.requireNonNull(waxedBlockFactory);
/* 36 */     Objects.requireNonNull(waxedBlockFactory);
/* 37 */     Objects.requireNonNull(waxedBlockFactory); return new WeatheringCopperBlocks((Block)register.apply(id, p -> (Block)weatheringFactory.apply(WeatheringCopper.WeatherState.UNAFFECTED, p), (BlockBehaviour.Properties)propertiesSupplier.apply(WeatheringCopper.WeatherState.UNAFFECTED)), (Block)register.apply("exposed_" + id, p -> (Block)weatheringFactory.apply(WeatheringCopper.WeatherState.EXPOSED, p), (BlockBehaviour.Properties)propertiesSupplier.apply(WeatheringCopper.WeatherState.EXPOSED)), (Block)register.apply("weathered_" + id, p -> (Block)weatheringFactory.apply(WeatheringCopper.WeatherState.WEATHERED, p), (BlockBehaviour.Properties)propertiesSupplier.apply(WeatheringCopper.WeatherState.WEATHERED)), (Block)register.apply("oxidized_" + id, p -> (Block)weatheringFactory.apply(WeatheringCopper.WeatherState.OXIDIZED, p), (BlockBehaviour.Properties)propertiesSupplier.apply(WeatheringCopper.WeatherState.OXIDIZED)), (Block)register.apply("waxed_" + id, waxedBlockFactory::apply, (BlockBehaviour.Properties)propertiesSupplier.apply(WeatheringCopper.WeatherState.UNAFFECTED)), (Block)register.apply("waxed_exposed_" + id, waxedBlockFactory::apply, (BlockBehaviour.Properties)propertiesSupplier.apply(WeatheringCopper.WeatherState.EXPOSED)), (Block)register.apply("waxed_weathered_" + id, waxedBlockFactory::apply, (BlockBehaviour.Properties)propertiesSupplier.apply(WeatheringCopper.WeatherState.WEATHERED)), (Block)register.apply("waxed_oxidized_" + id, waxedBlockFactory::apply, (BlockBehaviour.Properties)propertiesSupplier.apply(WeatheringCopper.WeatherState.OXIDIZED)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public ImmutableBiMap<Block, Block> weatheringMapping() { return ImmutableBiMap.of(this.unaffected, this.exposed, this.exposed, this.weathered, this.weathered, this.oxidized); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public ImmutableBiMap<Block, Block> waxedMapping() { return ImmutableBiMap.of(this.unaffected, this.waxed, this.exposed, this.waxedExposed, this.weathered, this.waxedWeathered, this.oxidized, this.waxedOxidized); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   public ImmutableList<Block> asList() { return ImmutableList.of(this.unaffected, this.waxed, this.exposed, this.waxedExposed, this.weathered, this.waxedWeathered, this.oxidized, this.waxedOxidized); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void forEach(Consumer<Block> consumer) {
/* 68 */     consumer.accept(this.unaffected);
/* 69 */     consumer.accept(this.exposed);
/* 70 */     consumer.accept(this.weathered);
/* 71 */     consumer.accept(this.oxidized);
/* 72 */     consumer.accept(this.waxed);
/* 73 */     consumer.accept(this.waxedExposed);
/* 74 */     consumer.accept(this.waxedWeathered);
/* 75 */     consumer.accept(this.waxedOxidized);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WeatheringCopperBlocks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */