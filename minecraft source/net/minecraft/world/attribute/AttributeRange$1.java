/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import com.mojang.serialization.DataResult;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends Object
/*    */   implements AttributeRange<Value>
/*    */ {
/* 14 */   public DataResult<Value> validate(Value value) { return DataResult.success(value); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public Value sanitize(Value value) { return value; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\AttributeRange$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */