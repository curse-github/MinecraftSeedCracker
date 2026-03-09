/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityWolfColorFix
/*    */   extends NamedEntityFix {
/* 10 */   public EntityWolfColorFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType, "EntityWolfColorFix", References.ENTITY, "minecraft:wolf"); }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public Dynamic<?> fixTag(Dynamic<?> input) { return input.update("CollarColor", color -> color.createByte((byte)(15 - color.asInt(0)))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), this::fixTag); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityWolfColorFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */