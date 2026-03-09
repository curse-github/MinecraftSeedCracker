/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.CompoundTagArgument;
/*    */ import net.minecraft.commands.arguments.ResourceArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*    */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SummonCommand
/*    */ {
/* 35 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.summon.failed"));
/* 36 */   private static final SimpleCommandExceptionType ERROR_FAILED_PEACEFUL = new SimpleCommandExceptionType(Component.translatable("commands.summon.failed.peaceful"));
/* 37 */   private static final SimpleCommandExceptionType ERROR_DUPLICATE_UUID = new SimpleCommandExceptionType(Component.translatable("commands.summon.failed.uuid"));
/* 38 */   private static final SimpleCommandExceptionType INVALID_POSITION = new SimpleCommandExceptionType(Component.translatable("commands.summon.invalidPosition"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/* 41 */     dispatcher.register(
/* 42 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("summon")
/* 43 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 44 */         .then((
/* 45 */           (RequiredArgumentBuilder)Commands.argument("entity", ResourceArgument.resource(context, Registries.ENTITY_TYPE))
/* 46 */           .suggests(SuggestionProviders.cast(SuggestionProviders.SUMMONABLE_ENTITIES))
/* 47 */           .executes(c -> spawnEntity((CommandSourceStack)c.getSource(), ResourceArgument.getSummonableEntityType(c, "entity"), ((CommandSourceStack)c.getSource()).getPosition(), new CompoundTag(), true)))
/* 48 */           .then((
/* 49 */             (RequiredArgumentBuilder)Commands.argument("pos", Vec3Argument.vec3())
/* 50 */             .executes(c -> spawnEntity((CommandSourceStack)c.getSource(), ResourceArgument.getSummonableEntityType(c, "entity"), Vec3Argument.getVec3(c, "pos"), new CompoundTag(), true)))
/* 51 */             .then(
/* 52 */               Commands.argument("nbt", CompoundTagArgument.compoundTag())
/* 53 */               .executes(c -> spawnEntity((CommandSourceStack)c.getSource(), ResourceArgument.getSummonableEntityType(c, "entity"), Vec3Argument.getVec3(c, "pos"), CompoundTagArgument.getCompoundTag(c, "nbt"), false))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Entity createEntity(CommandSourceStack source, Holder.Reference<EntityType<?>> type, Vec3 pos, CompoundTag nbt, boolean finalize) throws CommandSyntaxException {
/* 61 */     BlockPos blockPos = BlockPos.containing(pos);
/* 62 */     if (!Level.isInSpawnableBounds(blockPos)) {
/* 63 */       throw INVALID_POSITION.create();
/*    */     }
/*    */     
/* 66 */     if (source.getLevel().getDifficulty() == Difficulty.PEACEFUL && !((EntityType)type.value()).isAllowedInPeaceful()) {
/* 67 */       throw ERROR_FAILED_PEACEFUL.create();
/*    */     }
/*    */     
/* 70 */     CompoundTag entityTag = nbt.copy();
/* 71 */     entityTag.putString("id", type.key().identifier().toString());
/*    */     
/* 73 */     ServerLevel level = source.getLevel();
/* 74 */     Entity entity = EntityType.loadEntityRecursive(entityTag, level, EntitySpawnReason.COMMAND, e -> {
/* 75 */           e.snapTo(pos.x, pos.y, pos.z, e.getYRot(), e.getXRot());
/* 76 */           return e;
/*    */         });
/* 78 */     if (entity == null) {
/* 79 */       throw ERROR_FAILED.create();
/*    */     }
/*    */     
/* 82 */     if (finalize && entity instanceof Mob) { Mob mob = (Mob)entity;
/* 83 */       mob.finalizeSpawn(source.getLevel(), source.getLevel().getCurrentDifficultyAt(entity.blockPosition()), EntitySpawnReason.COMMAND, null); }
/*    */ 
/*    */     
/* 86 */     if (!level.tryAddFreshEntityWithPassengers(entity)) {
/* 87 */       throw ERROR_DUPLICATE_UUID.create();
/*    */     }
/* 89 */     return entity;
/*    */   }
/*    */   
/*    */   private static int spawnEntity(CommandSourceStack source, Holder.Reference<EntityType<?>> type, Vec3 pos, CompoundTag nbt, boolean finalize) throws CommandSyntaxException {
/* 93 */     Entity entity = createEntity(source, type, pos, nbt, finalize);
/*    */     
/* 95 */     source.sendSuccess(() -> Component.translatable("commands.summon.success", new Object[] { entity.getDisplayName() }), true);
/* 96 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\SummonCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */