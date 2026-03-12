/*    */ package net.minecraft.nbt;
/*    */ public class TagTypes {
/*    */   private static final TagType<?>[] TYPES = { 
/*  4 */       EndTag.TYPE, ByteTag.TYPE, ShortTag.TYPE, IntTag.TYPE, LongTag.TYPE, FloatTag.TYPE, DoubleTag.TYPE, ByteArrayTag.TYPE, StringTag.TYPE, ListTag.TYPE, CompoundTag.TYPE, IntArrayTag.TYPE, LongArrayTag.TYPE };
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
/*    */   public static TagType<?> getType(int typeId) {
/* 21 */     if (typeId < 0 || typeId >= TYPES.length) {
/* 22 */       return TagType.createInvalid(typeId);
/*    */     }
/*    */     
/* 25 */     return TYPES[typeId];
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\TagTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */