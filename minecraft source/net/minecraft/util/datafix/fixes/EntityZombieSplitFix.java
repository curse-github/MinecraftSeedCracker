/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.base.Suppliers;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class EntityZombieSplitFix
/*    */   extends EntityRenameFix {
/*    */   private final Supplier<Type<?>> zombieVillagerType;
/*    */   
/*    */   public EntityZombieSplitFix(Schema outputSchema) {
/* 18 */     super("EntityZombieSplitFix", outputSchema, true);
/*    */     
/* 20 */     this.zombieVillagerType = Suppliers.memoize(() -> getOutputSchema().getChoiceType(References.ENTITY, "ZombieVillager"));
/*    */   }
/*    */   protected Pair<String, Typed<?>> fix(String name, Typed<?> entity) {
/*    */     Typed<?> newEntity, newEntity;
/*    */     String newName, newName;
/* 25 */     if (!name.equals("Zombie")) {
/* 26 */       return Pair.of(name, entity);
/*    */     }
/*    */     
/* 29 */     Dynamic<?> tag = (Dynamic)entity.getOptional(DSL.remainderFinder()).orElseThrow();
/* 30 */     int type = tag.get("ZombieType").asInt(0);
/*    */ 
/*    */ 
/*    */     
/* 34 */     switch (type)
/*    */     { default:
/* 36 */         newName = "Zombie";
/* 37 */         newEntity = entity;
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
/* 49 */         return Pair.of(newName, newEntity.update(DSL.remainderFinder(), e -> e.remove("ZombieType")));case 1: case 2: case 3: case 4: case 5: newName = "ZombieVillager"; newEntity = changeSchemaToZombieVillager(entity, type - 1); return Pair.of(newName, newEntity.update(DSL.remainderFinder(), e -> e.remove("ZombieType")));case 6: break; }  String newName = "Husk"; Typed<?> newEntity = entity; return Pair.of(newName, newEntity.update(DSL.remainderFinder(), e -> e.remove("ZombieType")));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 54 */   private Typed<?> changeSchemaToZombieVillager(Typed<?> entity, int profession) { return Util.writeAndReadTypedOrThrow(entity, (Type)this.zombieVillagerType.get(), serializedEntity -> serializedEntity.set("Profession", serializedEntity.createInt(profession))); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityZombieSplitFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */