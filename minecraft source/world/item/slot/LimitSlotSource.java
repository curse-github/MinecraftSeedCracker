/*    */ package net.minecraft.world.item.slot;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class LimitSlotSource extends TransformedSlotSource {
/*  8 */   public static final MapCodec<LimitSlotSource> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(ExtraCodecs.POSITIVE_INT
/*  9 */         .fieldOf("limit").forGetter(()))
/* 10 */       .apply(i, LimitSlotSource::new));
/*    */   
/*    */   private final int limit;
/*    */   
/*    */   private LimitSlotSource(SlotSource slotSource, int limit) {
/* 15 */     super(slotSource);
/* 16 */     this.limit = limit;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public MapCodec<LimitSlotSource> codec() { return MAP_CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   protected SlotCollection transform(SlotCollection slots) { return slots.limit(this.limit); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\slot\LimitSlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */