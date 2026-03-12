/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class GroupSlotSource extends CompositeSlotSource {
/*  9 */   public static final MapCodec<GroupSlotSource> MAP_CODEC = createCodec(GroupSlotSource::new);
/* 10 */   public static final Codec<GroupSlotSource> INLINE_CODEC = createInlineCodec(GroupSlotSource::new);
/*    */ 
/*    */   
/* 13 */   private GroupSlotSource(List<SlotSource> terms) { super(terms); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public MapCodec<GroupSlotSource> codec() { return MAP_CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\slot\GroupSlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */