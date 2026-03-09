/*    */ package net.minecraft.network.chat.numbers;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class BlankFormat implements NumberFormat {
/* 10 */   public static final BlankFormat INSTANCE = new BlankFormat();
/*    */   
/* 12 */   public static final NumberFormatType<BlankFormat> TYPE = new NumberFormatType<BlankFormat>() {
/* 13 */       private static final MapCodec<BlankFormat> CODEC = MapCodec.unit(BlankFormat.INSTANCE);
/*    */       
/* 15 */       private static final StreamCodec<RegistryFriendlyByteBuf, BlankFormat> STREAM_CODEC = StreamCodec.unit(BlankFormat.INSTANCE);
/*    */ 
/*    */ 
/*    */       
/* 19 */       public MapCodec<BlankFormat> mapCodec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 24 */       public StreamCodec<RegistryFriendlyByteBuf, BlankFormat> streamCodec() { return STREAM_CODEC; }
/*    */     };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public MutableComponent format(int value) { return Component.empty(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public NumberFormatType<BlankFormat> type() { return TYPE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\numbers\BlankFormat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */