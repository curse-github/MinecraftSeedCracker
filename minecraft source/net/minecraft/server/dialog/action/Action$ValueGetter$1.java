/*    */ package net.minecraft.server.dialog.action;
/*    */ 
/*    */ import net.minecraft.nbt.StringTag;
/*    */ import net.minecraft.nbt.Tag;
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
/*    */ class null
/*    */   implements Action.ValueGetter
/*    */ {
/* 37 */   public String asTemplateSubstitution() { return value; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public Tag asTag() { return StringTag.valueOf(value); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\action\Action$ValueGetter$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */