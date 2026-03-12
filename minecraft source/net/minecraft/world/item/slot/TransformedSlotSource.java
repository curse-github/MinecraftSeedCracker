/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public abstract class TransformedSlotSource
/*    */   implements SlotSource {
/*    */   protected final SlotSource slotSource;
/*    */   
/* 14 */   protected TransformedSlotSource(SlotSource slotSource) { this.slotSource = slotSource; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected static <T extends TransformedSlotSource> Products.P1<RecordCodecBuilder.Mu<T>, SlotSource> commonFields(RecordCodecBuilder.Instance<T> i) {
/* 21 */     return i.group(SlotSources.CODEC
/* 22 */         .fieldOf("slot_source").forGetter(t -> t.slotSource));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public final SlotCollection provide(LootContext context) { return transform(this.slotSource.provide(context)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 35 */     super.validate(context);
/* 36 */     this.slotSource.validate(context.forChild(new ProblemReporter.FieldPathElement("slot_source")));
/*    */   }
/*    */   
/*    */   public abstract MapCodec<? extends TransformedSlotSource> codec();
/*    */   
/*    */   protected abstract SlotCollection transform(SlotCollection paramSlotCollection);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\slot\TransformedSlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */