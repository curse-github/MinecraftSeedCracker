/*    */ package net.minecraft.data.advancements.packs;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.advancements.Advancement;
/*    */ import net.minecraft.advancements.AdvancementHolder;
/*    */ import net.minecraft.advancements.AdvancementRewards;
/*    */ import net.minecraft.advancements.AdvancementType;
/*    */ import net.minecraft.advancements.criterion.ChangeDimensionTrigger;
/*    */ import net.minecraft.advancements.criterion.DistancePredicate;
/*    */ import net.minecraft.advancements.criterion.EnterBlockTrigger;
/*    */ import net.minecraft.advancements.criterion.EntityPredicate;
/*    */ import net.minecraft.advancements.criterion.InventoryChangeTrigger;
/*    */ import net.minecraft.advancements.criterion.KilledTrigger;
/*    */ import net.minecraft.advancements.criterion.LevitationTrigger;
/*    */ import net.minecraft.advancements.criterion.LocationPredicate;
/*    */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*    */ import net.minecraft.advancements.criterion.PlayerTrigger;
/*    */ import net.minecraft.advancements.criterion.SummonedEntityTrigger;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.advancements.AdvancementSubProvider;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
/*    */ 
/*    */ 
/*    */ public class VanillaTheEndAdvancements
/*    */   implements AdvancementSubProvider
/*    */ {
/*    */   public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
/* 36 */     HolderLookup.RegistryLookup registryLookup = registries.lookupOrThrow(Registries.ENTITY_TYPE);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     AdvancementHolder root = Advancement.Builder.advancement().display(Blocks.END_STONE, Component.translatable("advancements.end.root.title"), Component.translatable("advancements.end.root.description"), Identifier.withDefaultNamespace("gui/advancements/backgrounds/end"), AdvancementType.TASK, false, false, false).addCriterion("entered_end", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.END)).save(output, "end/root");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 47 */     AdvancementHolder killDragon = Advancement.Builder.advancement().parent(root).display(Blocks.DRAGON_HEAD, Component.translatable("advancements.end.kill_dragon.title"), Component.translatable("advancements.end.kill_dragon.description"), null, AdvancementType.TASK, true, true, false).addCriterion("killed_dragon", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(registryLookup, EntityType.ENDER_DRAGON))).save(output, "end/kill_dragon");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 53 */     AdvancementHolder enterEndGateway = Advancement.Builder.advancement().parent(killDragon).display(Items.ENDER_PEARL, Component.translatable("advancements.end.enter_end_gateway.title"), Component.translatable("advancements.end.enter_end_gateway.description"), null, AdvancementType.TASK, true, true, false).addCriterion("entered_end_gateway", EnterBlockTrigger.TriggerInstance.entersBlock(Blocks.END_GATEWAY)).save(output, "end/enter_end_gateway");
/*    */     
/* 55 */     Advancement.Builder.advancement()
/* 56 */       .parent(killDragon)
/* 57 */       .display(Items.END_CRYSTAL, Component.translatable("advancements.end.respawn_dragon.title"), Component.translatable("advancements.end.respawn_dragon.description"), null, AdvancementType.GOAL, true, true, false)
/* 58 */       .addCriterion("summoned_dragon", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(registryLookup, EntityType.ENDER_DRAGON)))
/* 59 */       .save(output, "end/respawn_dragon");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 65 */     AdvancementHolder findEndCity = Advancement.Builder.advancement().parent(enterEndGateway).display(Blocks.PURPUR_BLOCK, Component.translatable("advancements.end.find_end_city.title"), Component.translatable("advancements.end.find_end_city.description"), null, AdvancementType.TASK, true, true, false).addCriterion("in_city", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(registries.lookupOrThrow(Registries.STRUCTURE).getOrThrow(BuiltinStructures.END_CITY)))).save(output, "end/find_end_city");
/*    */     
/* 67 */     Advancement.Builder.advancement()
/* 68 */       .parent(killDragon)
/* 69 */       .display(Items.DRAGON_BREATH, Component.translatable("advancements.end.dragon_breath.title"), Component.translatable("advancements.end.dragon_breath.description"), null, AdvancementType.GOAL, true, true, false)
/* 70 */       .addCriterion("dragon_breath", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { Items.DRAGON_BREATH
/* 71 */           })).save(output, "end/dragon_breath");
/*    */     
/* 73 */     Advancement.Builder.advancement()
/* 74 */       .parent(findEndCity)
/* 75 */       .display(Items.SHULKER_SHELL, Component.translatable("advancements.end.levitate.title"), Component.translatable("advancements.end.levitate.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 76 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 77 */       .addCriterion("levitated", LevitationTrigger.TriggerInstance.levitated(DistancePredicate.vertical(MinMaxBounds.Doubles.atLeast(50.0D))))
/* 78 */       .save(output, "end/levitate");
/*    */     
/* 80 */     Advancement.Builder.advancement()
/* 81 */       .parent(findEndCity)
/* 82 */       .display(Items.ELYTRA, Component.translatable("advancements.end.elytra.title"), Component.translatable("advancements.end.elytra.description"), null, AdvancementType.GOAL, true, true, false)
/* 83 */       .addCriterion("elytra", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { Items.ELYTRA
/* 84 */           })).save(output, "end/elytra");
/*    */     
/* 86 */     Advancement.Builder.advancement()
/* 87 */       .parent(killDragon)
/* 88 */       .display(Blocks.DRAGON_EGG, Component.translatable("advancements.end.dragon_egg.title"), Component.translatable("advancements.end.dragon_egg.description"), null, AdvancementType.GOAL, true, true, false)
/* 89 */       .addCriterion("dragon_egg", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { Blocks.DRAGON_EGG
/* 90 */           })).save(output, "end/dragon_egg");
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\advancements\packs\VanillaTheEndAdvancements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */