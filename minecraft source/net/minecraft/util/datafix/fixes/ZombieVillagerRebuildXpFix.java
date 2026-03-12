/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class ZombieVillagerRebuildXpFix
/*    */   extends NamedEntityFix {
/* 11 */   public ZombieVillagerRebuildXpFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType, "Zombie Villager XP rebuild", References.ENTITY, "minecraft:zombie_villager"); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 16 */     return entity.update(DSL.remainderFinder(), remainder -> {
/* 17 */           Optional<Number> xp = remainder.get("Xp").asNumber().result();
/* 18 */           if (xp.isEmpty()) {
/* 19 */             int level = remainder.get("VillagerData").get("level").asInt(1);
/* 20 */             return remainder.set("Xp", remainder.createInt(VillagerRebuildLevelAndXpFix.getMinXpPerLevel(level)));
/*    */           } 
/* 22 */           return remainder;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ZombieVillagerRebuildXpFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */