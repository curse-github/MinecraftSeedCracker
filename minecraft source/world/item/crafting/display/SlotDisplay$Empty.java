/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.context.ContextMap;
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
/*    */ public class Empty
/*    */   implements SlotDisplay
/*    */ {
/* 69 */   public static final Empty INSTANCE = new Empty();
/*    */   
/* 71 */   public static final MapCodec<Empty> MAP_CODEC = MapCodec.unit(INSTANCE);
/*    */   
/* 73 */   public static final StreamCodec<RegistryFriendlyByteBuf, Empty> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */   
/* 75 */   public static final SlotDisplay.Type<Empty> TYPE = new SlotDisplay.Type(MAP_CODEC, STREAM_CODEC);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 82 */   public SlotDisplay.Type<Empty> type() { return TYPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 87 */   public String toString() { return "<empty>"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 92 */   public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) { return Stream.empty(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\display\SlotDisplay$Empty.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */