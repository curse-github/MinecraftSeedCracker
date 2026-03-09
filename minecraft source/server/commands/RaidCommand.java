/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.Set;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.ComponentArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.entity.raid.Raids;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RaidCommand
/*     */ {
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  37 */     dispatcher.register(
/*  38 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("raid")
/*  39 */         .requires(Commands.hasPermission(Commands.LEVEL_ADMINS)))
/*  40 */         .then(Commands.literal("start")
/*  41 */           .then(
/*  42 */             Commands.argument("omenlvl", IntegerArgumentType.integer(0))
/*  43 */             .executes(c -> start((CommandSourceStack)c.getSource(), IntegerArgumentType.getInteger(c, "omenlvl"))))))
/*     */ 
/*     */         
/*  46 */         .then(Commands.literal("stop").executes(c -> stop((CommandSourceStack)c.getSource()))))
/*  47 */         .then(Commands.literal("check").executes(c -> check((CommandSourceStack)c.getSource()))))
/*  48 */         .then(Commands.literal("sound")
/*  49 */           .then(
/*  50 */             Commands.argument("type", ComponentArgument.textComponent(context))
/*  51 */             .executes(c -> playSound((CommandSourceStack)c.getSource(), ComponentArgument.getResolvedComponent(c, "type"))))))
/*     */         
/*  53 */         .then(Commands.literal("spawnleader").executes(c -> spawnLeader((CommandSourceStack)c.getSource()))))
/*  54 */         .then(Commands.literal("setomen").then(
/*  55 */             Commands.argument("level", IntegerArgumentType.integer(0))
/*  56 */             .executes(c -> setRaidOmenLevel((CommandSourceStack)c.getSource(), IntegerArgumentType.getInteger(c, "level"))))))
/*     */ 
/*     */         
/*  59 */         .then(Commands.literal("glow").executes(c -> glow((CommandSourceStack)c.getSource()))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static int glow(CommandSourceStack source) throws CommandSyntaxException {
/*  64 */     Raid raid = getRaid(source.getPlayerOrException());
/*     */     
/*  66 */     if (raid != null) {
/*  67 */       Set<Raider> raiders = raid.getAllRaiders();
/*  68 */       for (Raider raider : raiders) {
/*  69 */         raider.addEffect(new MobEffectInstance(MobEffects.GLOWING, 1000, 1));
/*     */       }
/*     */     } 
/*  72 */     return 1;
/*     */   }
/*     */   
/*     */   private static int setRaidOmenLevel(CommandSourceStack source, int level) throws CommandSyntaxException {
/*  76 */     Raid raid = getRaid(source.getPlayerOrException());
/*     */     
/*  78 */     if (raid != null) {
/*  79 */       int max = raid.getMaxRaidOmenLevel();
/*  80 */       if (level > max) {
/*  81 */         source.sendFailure(Component.literal("Sorry, the max raid omen level you can set is " + max));
/*     */       } else {
/*  83 */         int before = raid.getRaidOmenLevel();
/*  84 */         raid.setRaidOmenLevel(level);
/*  85 */         source.sendSuccess(() -> Component.literal("Changed village's raid omen level from " + before + " to " + level), false);
/*     */       } 
/*     */     } else {
/*  88 */       source.sendFailure(Component.literal("No raid found here"));
/*     */     } 
/*     */     
/*  91 */     return 1;
/*     */   }
/*     */   
/*     */   private static int spawnLeader(CommandSourceStack source) throws CommandSyntaxException {
/*  95 */     source.sendSuccess(() -> Component.literal("Spawned a raid captain"), false);
/*     */     
/*  97 */     Raider raider = (Raider)EntityType.PILLAGER.create(source.getLevel(), EntitySpawnReason.COMMAND);
/*  98 */     if (raider == null) {
/*  99 */       source.sendFailure(Component.literal("Pillager failed to spawn"));
/* 100 */       return 0;
/*     */     } 
/* 102 */     raider.setPatrolLeader(true);
/* 103 */     raider.setItemSlot(EquipmentSlot.HEAD, Raid.getOminousBannerInstance(source.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN)));
/* 104 */     raider.setPos((source.getPosition()).x, (source.getPosition()).y, (source.getPosition()).z);
/* 105 */     raider.finalizeSpawn(source.getLevel(), source.getLevel().getCurrentDifficultyAt(BlockPos.containing(source.getPosition())), EntitySpawnReason.COMMAND, null);
/* 106 */     source.getLevel().addFreshEntityWithPassengers(raider);
/*     */     
/* 108 */     return 1;
/*     */   }
/*     */   
/*     */   private static int playSound(CommandSourceStack source, Component type) {
/* 112 */     if (type != null && type.getString().equals("local")) {
/* 113 */       ServerLevel level = source.getLevel();
/* 114 */       Vec3 pos = source.getPosition().add(5.0D, 0.0D, 0.0D);
/* 115 */       level.playSeededSound(null, pos.x, pos.y, pos.z, SoundEvents.RAID_HORN, SoundSource.NEUTRAL, 2.0F, 1.0F, level.random.nextLong());
/*     */     } 
/* 117 */     return 1;
/*     */   }
/*     */   
/*     */   private static int start(CommandSourceStack source, int raidOmenLevel) throws CommandSyntaxException {
/* 121 */     ServerPlayer player = source.getPlayerOrException();
/* 122 */     BlockPos pos = player.blockPosition();
/*     */     
/* 124 */     if (player.level().isRaided(pos)) {
/* 125 */       source.sendFailure(Component.literal("Raid already started close by"));
/* 126 */       return -1;
/*     */     } 
/*     */     
/* 129 */     Raids raids = player.level().getRaids();
/* 130 */     Raid raid = raids.createOrExtendRaid(player, player.blockPosition());
/* 131 */     if (raid != null) {
/* 132 */       raid.setRaidOmenLevel(raidOmenLevel);
/* 133 */       raids.setDirty();
/* 134 */       source.sendSuccess(() -> Component.literal("Created a raid in your local village"), false);
/*     */     } else {
/* 136 */       source.sendFailure(Component.literal("Failed to create a raid in your local village"));
/*     */     } 
/* 138 */     return 1;
/*     */   }
/*     */   
/*     */   private static int stop(CommandSourceStack source) throws CommandSyntaxException {
/* 142 */     ServerPlayer player = source.getPlayerOrException();
/* 143 */     BlockPos pos = player.blockPosition();
/*     */     
/* 145 */     Raid raid = player.level().getRaidAt(pos);
/*     */     
/* 147 */     if (raid != null) {
/* 148 */       raid.stop();
/* 149 */       source.sendSuccess(() -> Component.literal("Stopped raid"), false);
/* 150 */       return 1;
/*     */     } 
/* 152 */     source.sendFailure(Component.literal("No raid here"));
/* 153 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int check(CommandSourceStack source) throws CommandSyntaxException {
/* 158 */     Raid raid = getRaid(source.getPlayerOrException());
/*     */     
/* 160 */     if (raid != null) {
/* 161 */       StringBuilder status = new StringBuilder();
/* 162 */       status.append("Found a started raid! ");
/* 163 */       source.sendSuccess(() -> Component.literal(status.toString()), false);
/* 164 */       StringBuilder status2 = new StringBuilder();
/* 165 */       status2.append("Num groups spawned: ");
/* 166 */       status2.append(raid.getGroupsSpawned());
/* 167 */       status2.append(" Raid omen level: ");
/* 168 */       status2.append(raid.getRaidOmenLevel());
/* 169 */       status2.append(" Num mobs: ");
/* 170 */       status2.append(raid.getTotalRaidersAlive());
/* 171 */       status2.append(" Raid health: ");
/* 172 */       status2.append(raid.getHealthOfLivingRaiders());
/* 173 */       status2.append(" / ");
/* 174 */       status2.append(raid.getTotalHealth());
/* 175 */       source.sendSuccess(() -> Component.literal(status2.toString()), false);
/* 176 */       return 1;
/*     */     } 
/* 178 */     source.sendFailure(Component.literal("Found no started raids"));
/* 179 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 184 */   private static Raid getRaid(ServerPlayer player) { return player.level().getRaidAt(player.blockPosition()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\RaidCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */