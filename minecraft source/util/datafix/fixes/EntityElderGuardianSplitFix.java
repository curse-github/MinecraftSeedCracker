/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityElderGuardianSplitFix
/*    */   extends SimpleEntityRenameFix
/*    */ {
/* 11 */   public EntityElderGuardianSplitFix(Schema outputSchema, boolean changesType) { super("EntityElderGuardianSplitFix", outputSchema, changesType); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   protected Pair<String, Dynamic<?>> getNewNameAndTag(String name, Dynamic<?> tag) { return Pair.of((Objects.equals(name, "Guardian") && tag.get("Elder").asBoolean(false)) ? "ElderGuardian" : name, tag); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityElderGuardianSplitFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */