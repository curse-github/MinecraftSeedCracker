/*    */ package net.minecraft.world.item.slot;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulator;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulators;
/*    */ 
/*    */ public class ContentsSlotSource extends TransformedSlotSource {
/*  9 */   public static final MapCodec<ContentsSlotSource> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(ContainerComponentManipulators.CODEC
/* 10 */         .fieldOf("component").forGetter(()))
/* 11 */       .apply(i, ContentsSlotSource::new));
/*    */   
/*    */   private final ContainerComponentManipulator<?> component;
/*    */   
/*    */   private ContentsSlotSource(SlotSource slotSource, ContainerComponentManipulator<?> component) {
/* 16 */     super(slotSource);
/* 17 */     this.component = component;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 22 */   public MapCodec<ContentsSlotSource> codec() { return MAP_CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   protected SlotCollection transform(SlotCollection slots) { Objects.requireNonNull(this.component); return slots.flatMap(this.component::getSlots); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\slot\ContentsSlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */