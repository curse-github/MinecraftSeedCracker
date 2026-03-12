/*    */ package net.minecraft.world.item;
/*    */ import net.minecraft.world.level.block.WeatheringCopperBlocks;
/*    */ 
/*    */ public final class WeatheringCopperItems extends Record {
/*    */   private final Item unaffected;
/*    */   private final Item exposed;
/*    */   private final Item weathered;
/*    */   private final Item oxidized;
/*    */   
/* 10 */   public WeatheringCopperItems(Item unaffected, Item exposed, Item weathered, Item oxidized, Item waxed, Item waxedExposed, Item waxedWeathered, Item waxedOxidized) { this.unaffected = unaffected; this.exposed = exposed; this.weathered = weathered; this.oxidized = oxidized; this.waxed = waxed; this.waxedExposed = waxedExposed; this.waxedWeathered = waxedWeathered; this.waxedOxidized = waxedOxidized; } private final Item waxed; private final Item waxedExposed; private final Item waxedWeathered; private final Item waxedOxidized; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/WeatheringCopperItems;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/WeatheringCopperItems; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/WeatheringCopperItems;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/WeatheringCopperItems; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/WeatheringCopperItems;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/WeatheringCopperItems;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public Item unaffected() { return this.unaffected; } public Item exposed() { return this.exposed; } public Item weathered() { return this.weathered; } public Item oxidized() { return this.oxidized; } public Item waxed() { return this.waxed; } public Item waxedExposed() { return this.waxedExposed; } public Item waxedWeathered() { return this.waxedWeathered; } public Item waxedOxidized() { return this.waxedOxidized; }
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
/*    */   public static WeatheringCopperItems create(WeatheringCopperBlocks blocks, Function<Block, Item> itemFactory) {
/* 22 */     return new WeatheringCopperItems((Item)itemFactory
/* 23 */         .apply(blocks.unaffected()), (Item)itemFactory
/* 24 */         .apply(blocks.exposed()), (Item)itemFactory
/* 25 */         .apply(blocks.weathered()), (Item)itemFactory
/* 26 */         .apply(blocks.oxidized()), (Item)itemFactory
/* 27 */         .apply(blocks.waxed()), (Item)itemFactory
/* 28 */         .apply(blocks.waxedExposed()), (Item)itemFactory
/* 29 */         .apply(blocks.waxedWeathered()), (Item)itemFactory
/* 30 */         .apply(blocks.waxedOxidized()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public ImmutableBiMap<Item, Item> waxedMapping() { return ImmutableBiMap.of(this.unaffected, this.waxed, this.exposed, this.waxedExposed, this.weathered, this.waxedWeathered, this.oxidized, this.waxedOxidized); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void forEach(Consumer<Item> consumer) {
/* 44 */     consumer.accept(this.unaffected);
/* 45 */     consumer.accept(this.exposed);
/* 46 */     consumer.accept(this.weathered);
/* 47 */     consumer.accept(this.oxidized);
/* 48 */     consumer.accept(this.waxed);
/* 49 */     consumer.accept(this.waxedExposed);
/* 50 */     consumer.accept(this.waxedWeathered);
/* 51 */     consumer.accept(this.waxedOxidized);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\WeatheringCopperItems.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */