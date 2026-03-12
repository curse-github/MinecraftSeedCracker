/*    */ package net.minecraft.network.codec;
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
/*    */ class null
/*    */   extends Object
/*    */   implements StreamCodec<B, V>
/*    */ {
/* 26 */   public V decode(B input) { return (V)decoder.decode(input); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public void encode(B output, V value) { encoder.encode(output, value); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\codec\StreamCodec$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */