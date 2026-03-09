/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
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
/*    */ public interface StaticSize<T extends Tag>
/*    */   extends TagType<T>
/*    */ {
/* 31 */   default void skip(DataInput input, NbtAccounter accounter) throws IOException { input.skipBytes(size()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   default void skip(DataInput input, int count, NbtAccounter accounter) throws IOException { input.skipBytes(size() * count); }
/*    */   
/*    */   int size();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\TagType$StaticSize.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */