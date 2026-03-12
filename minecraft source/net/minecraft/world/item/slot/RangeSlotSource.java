/*    */ package net.minecraft.world.item.slot;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.entity.SlotProvider;
/*    */ import net.minecraft.world.inventory.SlotRange;
/*    */ import net.minecraft.world.inventory.SlotRanges;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootContextArg;
/*    */ 
/*    */ public class RangeSlotSource implements SlotSource {
/* 15 */   public static final MapCodec<RangeSlotSource> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(LootContextArg.ENTITY_OR_BLOCK
/* 16 */         .fieldOf("source").forGetter(()), SlotRanges.CODEC
/* 17 */         .fieldOf("slots").forGetter(()))
/* 18 */       .apply(i, RangeSlotSource::new));
/*    */   
/*    */   private final LootContextArg<Object> source;
/*    */   private final SlotRange slotRange;
/*    */   
/*    */   private RangeSlotSource(LootContextArg<Object> source, SlotRange slotRange) {
/* 24 */     this.source = source;
/* 25 */     this.slotRange = slotRange;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public MapCodec<RangeSlotSource> codec() { return MAP_CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public Set<ContextKey<?>> getReferencedContextParams() { return Set.of(this.source.contextParam()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public final SlotCollection provide(LootContext context) {
/* 40 */     Object maybeProvider = this.source.get(context);
/*    */     
/* 42 */     if (maybeProvider instanceof SlotProvider) { SlotProvider slotProvider = (SlotProvider)maybeProvider;
/* 43 */       return slotProvider.getSlotsFromRange(this.slotRange.slots()); }
/*    */     
/* 45 */     return SlotCollection.EMPTY;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\slot\RangeSlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */