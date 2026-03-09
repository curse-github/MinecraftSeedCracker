/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.escape.Escaper;
/*    */ import com.google.common.escape.Escapers;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class LockComponentPredicateFix
/*    */   extends DataComponentRemainderFix
/*    */ {
/* 12 */   public static final Escaper ESCAPER = Escapers.builder()
/* 13 */     .addEscape('"', "\\\"")
/* 14 */     .addEscape('\\', "\\\\")
/* 15 */     .build();
/*    */ 
/*    */   
/* 18 */   public LockComponentPredicateFix(Schema outputSchema) { super(outputSchema, "LockComponentPredicateFix", "minecraft:lock"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   protected <T> Dynamic<T> fixComponent(Dynamic<T> input) { return fixLock(input); }
/*    */ 
/*    */   
/*    */   public static <T> Dynamic<T> fixLock(Dynamic<T> input) {
/* 27 */     Optional<String> name = input.asString().result();
/* 28 */     if (name.isEmpty()) {
/* 29 */       return null;
/*    */     }
/*    */     
/* 32 */     if (((String)name.get()).isEmpty()) {
/* 33 */       return null;
/*    */     }
/* 35 */     Dynamic<T> nameComponent = input.createString("\"" + ESCAPER.escape((String)name.get()) + "\"");
/* 36 */     Dynamic<T> components = input.emptyMap().set("minecraft:custom_name", nameComponent);
/* 37 */     return input.emptyMap().set("components", components);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\LockComponentPredicateFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */