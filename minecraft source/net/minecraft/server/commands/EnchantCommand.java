/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.ResourceArgument;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EnchantCommand
/*    */ {
/* 32 */   private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType(target -> Component.translatableEscape("commands.enchant.failed.entity", new Object[] { target }));
/* 33 */   private static final DynamicCommandExceptionType ERROR_NO_ITEM = new DynamicCommandExceptionType(target -> Component.translatableEscape("commands.enchant.failed.itemless", new Object[] { target }));
/* 34 */   private static final DynamicCommandExceptionType ERROR_INCOMPATIBLE = new DynamicCommandExceptionType(item -> Component.translatableEscape("commands.enchant.failed.incompatible", new Object[] { item }));
/* 35 */   private static final Dynamic2CommandExceptionType ERROR_LEVEL_TOO_HIGH = new Dynamic2CommandExceptionType((level, max) -> Component.translatableEscape("commands.enchant.failed.level", new Object[] { level, max }));
/* 36 */   private static final SimpleCommandExceptionType ERROR_NOTHING_HAPPENED = new SimpleCommandExceptionType(Component.translatable("commands.enchant.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/* 39 */     dispatcher.register(
/* 40 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("enchant")
/* 41 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 42 */         .then(
/* 43 */           Commands.argument("targets", EntityArgument.entities())
/* 44 */           .then((
/* 45 */             (RequiredArgumentBuilder)Commands.argument("enchantment", ResourceArgument.resource(context, Registries.ENCHANTMENT))
/* 46 */             .executes(c -> enchant((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), ResourceArgument.getEnchantment(c, "enchantment"), 1)))
/* 47 */             .then(
/* 48 */               Commands.argument("level", IntegerArgumentType.integer(0))
/* 49 */               .executes(c -> enchant((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), ResourceArgument.getEnchantment(c, "enchantment"), IntegerArgumentType.getInteger(c, "level")))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int enchant(CommandSourceStack source, Collection<? extends Entity> targets, Holder<Enchantment> enchantmentHolder, int level) throws CommandSyntaxException {
/* 57 */     Enchantment enchantment = (Enchantment)enchantmentHolder.value();
/* 58 */     if (level > enchantment.getMaxLevel()) {
/* 59 */       throw ERROR_LEVEL_TOO_HIGH.create(Integer.valueOf(level), Integer.valueOf(enchantment.getMaxLevel()));
/*    */     }
/*    */     
/* 62 */     int success = 0;
/*    */     
/* 64 */     for (Entity entity : targets) {
/* 65 */       if (entity instanceof LivingEntity) { LivingEntity target = (LivingEntity)entity;
/* 66 */         ItemStack item = target.getMainHandItem();
/* 67 */         if (!item.isEmpty()) {
/* 68 */           if (enchantment.canEnchant(item) && EnchantmentHelper.isEnchantmentCompatible(EnchantmentHelper.getEnchantmentsForCrafting(item).keySet(), enchantmentHolder)) {
/* 69 */             item.enchant(enchantmentHolder, level);
/* 70 */             success++; continue;
/* 71 */           }  if (targets.size() == 1)
/* 72 */             throw ERROR_INCOMPATIBLE.create(item.getHoverName().getString());  continue;
/*    */         } 
/* 74 */         if (targets.size() == 1)
/* 75 */           throw ERROR_NO_ITEM.create(target.getName().getString());  continue; }
/*    */       
/* 77 */       if (targets.size() == 1) {
/* 78 */         throw ERROR_NOT_LIVING_ENTITY.create(entity.getName().getString());
/*    */       }
/*    */     } 
/*    */     
/* 82 */     if (success == 0)
/* 83 */       throw ERROR_NOTHING_HAPPENED.create(); 
/* 84 */     if (targets.size() == 1) {
/* 85 */       source.sendSuccess(() -> Component.translatable("commands.enchant.success.single", new Object[] { Enchantment.getFullname(enchantmentHolder, level), ((Entity)targets.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 87 */       source.sendSuccess(() -> Component.translatable("commands.enchant.success.multiple", new Object[] { Enchantment.getFullname(enchantmentHolder, level), Integer.valueOf(targets.size()) }), true);
/*    */     } 
/*    */     
/* 90 */     return success;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\EnchantCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */