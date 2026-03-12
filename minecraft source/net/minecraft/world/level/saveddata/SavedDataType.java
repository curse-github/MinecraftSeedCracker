/*    */ package net.minecraft.world.level.saveddata;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ 
/*    */ public final class SavedDataType<T extends SavedData> extends Record {
/*    */   private final String id;
/*    */   private final Supplier<T> constructor;
/*    */   
/*  8 */   public SavedDataType(String id, Supplier<T> constructor, Codec<T> codec, DataFixTypes dataFixType) { this.id = id; this.constructor = constructor; this.codec = codec; this.dataFixType = dataFixType; } private final Codec<T> codec; private final DataFixTypes dataFixType; public String id() { return this.id; } public Supplier<T> constructor() { return this.constructor; } public Codec<T> codec() { return this.codec; } public DataFixTypes dataFixType() { return this.dataFixType; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   public boolean equals(Object obj) { if (obj instanceof SavedDataType) { SavedDataType<?> type = (SavedDataType)obj; if (this.id.equals(type.id)); }  return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public int hashCode() { return this.id.hashCode(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public String toString() { return "SavedDataType[" + this.id + "]"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\saveddata\SavedDataType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */