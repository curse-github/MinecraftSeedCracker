/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityTippedArrowFix
/*    */   extends SimplestEntityRenameFix
/*    */ {
/*  9 */   public EntityTippedArrowFix(Schema outputSchema, boolean changesType) { super("EntityTippedArrowFix", outputSchema, changesType); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   protected String rename(String name) { return Objects.equals(name, "TippedArrow") ? "Arrow" : name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityTippedArrowFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */