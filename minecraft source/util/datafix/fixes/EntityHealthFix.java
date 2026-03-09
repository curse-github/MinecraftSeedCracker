/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class EntityHealthFix
/*    */   extends DataFix {
/* 15 */   public EntityHealthFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*    */ 
/*    */   
/* 18 */   private static final Set<String> ENTITIES = Sets.newHashSet(new String[] { "ArmorStand", "Bat", "Blaze", "CaveSpider", "Chicken", "Cow", "Creeper", "EnderDragon", "Enderman", "Endermite", "EntityHorse", "Ghast", "Giant", "Guardian", "LavaSlime", "MushroomCow", "Ozelot", "Pig", "PigZombie", "Rabbit", "Sheep", "Shulker", "Silverfish", "Skeleton", "Slime", "SnowMan", "Spider", "Squid", "Villager", "VillagerGolem", "Witch", "WitherBoss", "Wolf", "Zombie" });
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> input) {
/*    */     float health;
/* 58 */     Optional<Number> oldHealF = input.get("HealF").asNumber().result();
/* 59 */     Optional<Number> oldHealth = input.get("Health").asNumber().result();
/* 60 */     if (oldHealF.isPresent()) {
/* 61 */       health = ((Number)oldHealF.get()).floatValue();
/* 62 */       input = input.remove("HealF");
/* 63 */     } else if (oldHealth.isPresent()) {
/* 64 */       health = ((Number)oldHealth.get()).floatValue();
/*    */     } else {
/* 66 */       return input;
/*    */     } 
/* 68 */     return input.set("Health", input.createFloat(health));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 73 */   public TypeRewriteRule makeRule() { return fixTypeEverywhereTyped("EntityHealthFix", getInputSchema().getType(References.ENTITY), input -> input.update(DSL.remainderFinder(), this::fixTag)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityHealthFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */