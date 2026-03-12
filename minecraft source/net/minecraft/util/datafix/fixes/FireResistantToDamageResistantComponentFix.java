/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class FireResistantToDamageResistantComponentFix
/*    */   extends DataComponentRemainderFix {
/*  8 */   public FireResistantToDamageResistantComponentFix(Schema outputSchema) { super(outputSchema, "FireResistantToDamageResistantComponentFix", "minecraft:fire_resistant", "minecraft:damage_resistant"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 13 */   protected <T> Dynamic<T> fixComponent(Dynamic<T> input) { return input.emptyMap().set("types", input.createString("#minecraft:is_fire")); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\FireResistantToDamageResistantComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */