/*    */ package net.minecraft.server.dialog.action;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
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
/*    */ public interface ValueGetter
/*    */ {
/*    */   String asTemplateSubstitution();
/*    */   
/*    */   Tag asTag();
/*    */   
/* 30 */   static Map<String, String> getAsTemplateSubstitutions(Map<String, ValueGetter> parameters) { return Maps.transformValues(parameters, ValueGetter::asTemplateSubstitution); }
/*    */ 
/*    */   
/*    */   static ValueGetter of(final String value) {
/* 34 */     return new ValueGetter()
/*    */       {
/*    */         public String asTemplateSubstitution() {
/* 37 */           return value;
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 42 */         public Tag asTag() { return StringTag.valueOf(value); }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   static ValueGetter of(final Supplier<String> value) {
/* 48 */     return new ValueGetter()
/*    */       {
/*    */         public String asTemplateSubstitution() {
/* 51 */           return (String)value.get();
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 56 */         public Tag asTag() { return StringTag.valueOf((String)value.get()); }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\action\Action$ValueGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */