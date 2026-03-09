/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class RemoveGolemGossipFix
/*    */   extends NamedEntityFix {
/* 10 */   public RemoveGolemGossipFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType, "Remove Golem Gossip Fix", References.ENTITY, "minecraft:villager"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), RemoveGolemGossipFix::fixValue); }
/*    */ 
/*    */   
/*    */   private static Dynamic<?> fixValue(Dynamic<?> tag) {
/* 19 */     return tag.update("Gossips", gossips -> 
/* 20 */         tag.createList(gossips.asStream().filter(())));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\RemoveGolemGossipFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */