/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.Collection;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.ResourceArgument;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EffectCommands
/*     */ {
/*  34 */   private static final SimpleCommandExceptionType ERROR_GIVE_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.effect.give.failed"));
/*  35 */   private static final SimpleCommandExceptionType ERROR_CLEAR_EVERYTHING_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.effect.clear.everything.failed"));
/*  36 */   private static final SimpleCommandExceptionType ERROR_CLEAR_SPECIFIC_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.effect.clear.specific.failed"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  39 */     dispatcher.register(
/*  40 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("effect")
/*  41 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  42 */         .then((
/*  43 */           (LiteralArgumentBuilder)Commands.literal("clear")
/*  44 */           .executes(c -> clearEffects((CommandSourceStack)c.getSource(), ImmutableList.of(((CommandSourceStack)c.getSource()).getEntityOrException()))))
/*  45 */           .then((
/*  46 */             (RequiredArgumentBuilder)Commands.argument("targets", EntityArgument.entities())
/*  47 */             .executes(c -> clearEffects((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"))))
/*  48 */             .then(
/*  49 */               Commands.argument("effect", ResourceArgument.resource(context, Registries.MOB_EFFECT))
/*  50 */               .executes(c -> clearEffect((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), ResourceArgument.getMobEffect(c, "effect")))))))
/*     */ 
/*     */ 
/*     */         
/*  54 */         .then(
/*  55 */           Commands.literal("give")
/*  56 */           .then(
/*  57 */             Commands.argument("targets", EntityArgument.entities())
/*  58 */             .then((
/*  59 */               (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("effect", ResourceArgument.resource(context, Registries.MOB_EFFECT))
/*  60 */               .executes(c -> giveEffect((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), ResourceArgument.getMobEffect(c, "effect"), null, 0, true)))
/*  61 */               .then((
/*  62 */                 (RequiredArgumentBuilder)Commands.argument("seconds", IntegerArgumentType.integer(1, 1000000))
/*  63 */                 .executes(c -> giveEffect((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), ResourceArgument.getMobEffect(c, "effect"), Integer.valueOf(IntegerArgumentType.getInteger(c, "seconds")), 0, true)))
/*  64 */                 .then((
/*  65 */                   (RequiredArgumentBuilder)Commands.argument("amplifier", IntegerArgumentType.integer(0, 255))
/*  66 */                   .executes(c -> giveEffect((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), ResourceArgument.getMobEffect(c, "effect"), Integer.valueOf(IntegerArgumentType.getInteger(c, "seconds")), IntegerArgumentType.getInteger(c, "amplifier"), true)))
/*  67 */                   .then(
/*  68 */                     Commands.argument("hideParticles", BoolArgumentType.bool())
/*  69 */                     .executes(c -> giveEffect((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), ResourceArgument.getMobEffect(c, "effect"), Integer.valueOf(IntegerArgumentType.getInteger(c, "seconds")), IntegerArgumentType.getInteger(c, "amplifier"), !BoolArgumentType.getBool(c, "hideParticles")))))))
/*     */ 
/*     */ 
/*     */               
/*  73 */               .then((
/*  74 */                 (LiteralArgumentBuilder)Commands.literal("infinite")
/*  75 */                 .executes(c -> giveEffect((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), ResourceArgument.getMobEffect(c, "effect"), Integer.valueOf(-1), 0, true)))
/*  76 */                 .then((
/*  77 */                   (RequiredArgumentBuilder)Commands.argument("amplifier", IntegerArgumentType.integer(0, 255))
/*  78 */                   .executes(c -> giveEffect((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), ResourceArgument.getMobEffect(c, "effect"), Integer.valueOf(-1), IntegerArgumentType.getInteger(c, "amplifier"), true)))
/*  79 */                   .then(
/*  80 */                     Commands.argument("hideParticles", BoolArgumentType.bool())
/*  81 */                     .executes(c -> giveEffect((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), ResourceArgument.getMobEffect(c, "effect"), Integer.valueOf(-1), IntegerArgumentType.getInteger(c, "amplifier"), !BoolArgumentType.getBool(c, "hideParticles"))))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int giveEffect(CommandSourceStack source, Collection<? extends Entity> entities, Holder<MobEffect> effectHolder, Integer seconds, int amplifier, boolean particles) throws CommandSyntaxException {
/*     */     int duration;
/*  92 */     MobEffect effect = (MobEffect)effectHolder.value();
/*  93 */     int count = 0;
/*     */ 
/*     */     
/*  96 */     if (seconds != null) {
/*  97 */       if (effect.isInstantenous()) {
/*  98 */         duration = seconds.intValue();
/*  99 */       } else if (seconds.intValue() == -1) {
/* 100 */         duration = -1;
/*     */       } else {
/* 102 */         duration = seconds.intValue() * 20;
/*     */       }
/*     */     
/* 105 */     } else if (effect.isInstantenous()) {
/* 106 */       duration = 1;
/*     */     } else {
/* 108 */       duration = 600;
/*     */     } 
/*     */ 
/*     */     
/* 112 */     for (Entity entity : entities) {
/* 113 */       if (entity instanceof LivingEntity) {
/* 114 */         MobEffectInstance instance = new MobEffectInstance(effectHolder, duration, amplifier, false, particles);
/* 115 */         if (((LivingEntity)entity).addEffect(instance, source.getEntity())) {
/* 116 */           count++;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 121 */     if (count == 0) {
/* 122 */       throw ERROR_GIVE_FAILED.create();
/*     */     }
/*     */     
/* 125 */     if (entities.size() == 1) {
/* 126 */       source.sendSuccess(() -> Component.translatable("commands.effect.give.success.single", new Object[] { effect.getDisplayName(), ((Entity)entities.iterator().next()).getDisplayName(), Integer.valueOf(duration / 20) }), true);
/*     */     } else {
/* 128 */       source.sendSuccess(() -> Component.translatable("commands.effect.give.success.multiple", new Object[] { effect.getDisplayName(), Integer.valueOf(entities.size()), Integer.valueOf(duration / 20) }), true);
/*     */     } 
/*     */     
/* 131 */     return count;
/*     */   }
/*     */   
/*     */   private static int clearEffects(CommandSourceStack source, Collection<? extends Entity> entities) throws CommandSyntaxException {
/* 135 */     int count = 0;
/*     */     
/* 137 */     for (Entity entity : entities) {
/* 138 */       if (entity instanceof LivingEntity && (
/* 139 */         (LivingEntity)entity).removeAllEffects()) {
/* 140 */         count++;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 145 */     if (count == 0) {
/* 146 */       throw ERROR_CLEAR_EVERYTHING_FAILED.create();
/*     */     }
/*     */     
/* 149 */     if (entities.size() == 1) {
/* 150 */       source.sendSuccess(() -> Component.translatable("commands.effect.clear.everything.success.single", new Object[] { ((Entity)entities.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 152 */       source.sendSuccess(() -> Component.translatable("commands.effect.clear.everything.success.multiple", new Object[] { Integer.valueOf(entities.size()) }), true);
/*     */     } 
/*     */     
/* 155 */     return count;
/*     */   }
/*     */   
/*     */   private static int clearEffect(CommandSourceStack source, Collection<? extends Entity> entities, Holder<MobEffect> effectHolder) throws CommandSyntaxException {
/* 159 */     MobEffect effect = (MobEffect)effectHolder.value();
/* 160 */     int count = 0;
/*     */     
/* 162 */     for (Entity entity : entities) {
/* 163 */       if (entity instanceof LivingEntity && (
/* 164 */         (LivingEntity)entity).removeEffect(effectHolder)) {
/* 165 */         count++;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 170 */     if (count == 0) {
/* 171 */       throw ERROR_CLEAR_SPECIFIC_FAILED.create();
/*     */     }
/*     */     
/* 174 */     if (entities.size() == 1) {
/* 175 */       source.sendSuccess(() -> Component.translatable("commands.effect.clear.specific.success.single", new Object[] { effect.getDisplayName(), ((Entity)entities.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 177 */       source.sendSuccess(() -> Component.translatable("commands.effect.clear.specific.success.multiple", new Object[] { effect.getDisplayName(), Integer.valueOf(entities.size()) }), true);
/*     */     } 
/*     */     
/* 180 */     return count;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\EffectCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */