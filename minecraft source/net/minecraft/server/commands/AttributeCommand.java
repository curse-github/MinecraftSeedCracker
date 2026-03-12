/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.DoubleArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.IdentifierArgument;
/*     */ import net.minecraft.commands.arguments.ResourceArgument;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeMap;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ 
/*     */ 
/*     */ public class AttributeCommand
/*     */ {
/*  37 */   private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType(target -> Component.translatableEscape("commands.attribute.failed.entity", new Object[] { target }));
/*  38 */   private static final Dynamic2CommandExceptionType ERROR_NO_SUCH_ATTRIBUTE = new Dynamic2CommandExceptionType((target, attribute) -> Component.translatableEscape("commands.attribute.failed.no_attribute", new Object[] { target, attribute }));
/*  39 */   private static final Dynamic3CommandExceptionType ERROR_NO_SUCH_MODIFIER = new Dynamic3CommandExceptionType((target, attribute, modifier) -> Component.translatableEscape("commands.attribute.failed.no_modifier", new Object[] { attribute, target, modifier }));
/*  40 */   private static final Dynamic3CommandExceptionType ERROR_MODIFIER_ALREADY_PRESENT = new Dynamic3CommandExceptionType((target, attribute, modifier) -> Component.translatableEscape("commands.attribute.failed.modifier_already_present", new Object[] { modifier, attribute, target }));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  43 */     dispatcher.register(
/*  44 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("attribute")
/*  45 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  46 */         .then(
/*  47 */           Commands.argument("target", EntityArgument.entity())
/*  48 */           .then((
/*  49 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("attribute", ResourceArgument.resource(context, Registries.ATTRIBUTE))
/*  50 */             .then((
/*  51 */               (LiteralArgumentBuilder)Commands.literal("get")
/*  52 */               .executes(c -> getAttributeValue((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), ResourceArgument.getAttribute(c, "attribute"), 1.0D)))
/*  53 */               .then(
/*  54 */                 Commands.argument("scale", DoubleArgumentType.doubleArg())
/*  55 */                 .executes(c -> getAttributeValue((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), ResourceArgument.getAttribute(c, "attribute"), DoubleArgumentType.getDouble(c, "scale"))))))
/*     */ 
/*     */             
/*  58 */             .then((
/*  59 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("base")
/*  60 */               .then(
/*  61 */                 Commands.literal("set")
/*  62 */                 .then(
/*  63 */                   Commands.argument("value", DoubleArgumentType.doubleArg())
/*  64 */                   .executes(c -> setAttributeBase((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), ResourceArgument.getAttribute(c, "attribute"), DoubleArgumentType.getDouble(c, "value"))))))
/*     */ 
/*     */               
/*  67 */               .then((
/*  68 */                 (LiteralArgumentBuilder)Commands.literal("get")
/*  69 */                 .executes(c -> getAttributeBase((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), ResourceArgument.getAttribute(c, "attribute"), 1.0D)))
/*  70 */                 .then(
/*  71 */                   Commands.argument("scale", DoubleArgumentType.doubleArg())
/*  72 */                   .executes(c -> getAttributeBase((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), ResourceArgument.getAttribute(c, "attribute"), DoubleArgumentType.getDouble(c, "scale"))))))
/*     */ 
/*     */               
/*  75 */               .then(
/*  76 */                 Commands.literal("reset")
/*  77 */                 .executes(c -> resetAttributeBase((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), ResourceArgument.getAttribute(c, "attribute"))))))
/*     */ 
/*     */             
/*  80 */             .then((
/*  81 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("modifier")
/*  82 */               .then(
/*  83 */                 Commands.literal("add")
/*  84 */                 .then(
/*  85 */                   Commands.argument("id", IdentifierArgument.id())
/*  86 */                   .then((
/*  87 */                     (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("value", DoubleArgumentType.doubleArg())
/*  88 */                     .then(
/*  89 */                       Commands.literal("add_value")
/*  90 */                       .executes(c -> addModifier((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), ResourceArgument.getAttribute(c, "attribute"), IdentifierArgument.getId(c, "id"), DoubleArgumentType.getDouble(c, "value"), AttributeModifier.Operation.ADD_VALUE))))
/*     */                     
/*  92 */                     .then(
/*  93 */                       Commands.literal("add_multiplied_base")
/*  94 */                       .executes(c -> addModifier((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), ResourceArgument.getAttribute(c, "attribute"), IdentifierArgument.getId(c, "id"), DoubleArgumentType.getDouble(c, "value"), AttributeModifier.Operation.ADD_MULTIPLIED_BASE))))
/*     */                     
/*  96 */                     .then(
/*  97 */                       Commands.literal("add_multiplied_total")
/*  98 */                       .executes(c -> addModifier((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), ResourceArgument.getAttribute(c, "attribute"), IdentifierArgument.getId(c, "id"), DoubleArgumentType.getDouble(c, "value"), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)))))))
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 103 */               .then(
/* 104 */                 Commands.literal("remove")
/* 105 */                 .then(Commands.argument("id", IdentifierArgument.id())
/* 106 */                   .suggests((c, p) -> SharedSuggestionProvider.suggestResource(getAttributeModifiers(EntityArgument.getEntity(c, "target"), ResourceArgument.getAttribute(c, "attribute")), p))
/* 107 */                   .executes(c -> removeModifier((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), ResourceArgument.getAttribute(c, "attribute"), IdentifierArgument.getId(c, "id"))))))
/*     */ 
/*     */               
/* 110 */               .then(
/* 111 */                 Commands.literal("value")
/* 112 */                 .then(
/* 113 */                   Commands.literal("get")
/* 114 */                   .then((
/* 115 */                     (RequiredArgumentBuilder)Commands.argument("id", IdentifierArgument.id())
/* 116 */                     .suggests((c, p) -> SharedSuggestionProvider.suggestResource(getAttributeModifiers(EntityArgument.getEntity(c, "target"), ResourceArgument.getAttribute(c, "attribute")), p))
/* 117 */                     .executes(c -> getAttributeModifier((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), ResourceArgument.getAttribute(c, "attribute"), IdentifierArgument.getId(c, "id"), 1.0D)))
/* 118 */                     .then(
/* 119 */                       Commands.argument("scale", DoubleArgumentType.doubleArg())
/* 120 */                       .executes(c -> getAttributeModifier((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), ResourceArgument.getAttribute(c, "attribute"), IdentifierArgument.getId(c, "id"), DoubleArgumentType.getDouble(c, "scale")))))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static AttributeInstance getAttributeInstance(Entity target, Holder<Attribute> attribute) throws CommandSyntaxException {
/* 132 */     AttributeInstance attributeInstance = getLivingEntity(target).getAttributes().getInstance(attribute);
/* 133 */     if (attributeInstance == null) {
/* 134 */       throw ERROR_NO_SUCH_ATTRIBUTE.create(target.getName(), getAttributeDescription(attribute));
/*     */     }
/* 136 */     return attributeInstance;
/*     */   }
/*     */   
/*     */   private static LivingEntity getLivingEntity(Entity target) throws CommandSyntaxException {
/* 140 */     if (!(target instanceof LivingEntity)) {
/* 141 */       throw ERROR_NOT_LIVING_ENTITY.create(target.getName());
/*     */     }
/* 143 */     return (LivingEntity)target;
/*     */   }
/*     */   
/*     */   private static LivingEntity getEntityWithAttribute(Entity target, Holder<Attribute> attribute) throws CommandSyntaxException {
/* 147 */     LivingEntity livingEntity = getLivingEntity(target);
/* 148 */     if (!livingEntity.getAttributes().hasAttribute(attribute)) {
/* 149 */       throw ERROR_NO_SUCH_ATTRIBUTE.create(target.getName(), getAttributeDescription(attribute));
/*     */     }
/* 151 */     return livingEntity;
/*     */   }
/*     */   
/*     */   private static int getAttributeValue(CommandSourceStack source, Entity target, Holder<Attribute> attribute, double scale) throws CommandSyntaxException {
/* 155 */     LivingEntity livingEntity = getEntityWithAttribute(target, attribute);
/* 156 */     double result = livingEntity.getAttributeValue(attribute);
/* 157 */     source.sendSuccess(() -> Component.translatable("commands.attribute.value.get.success", new Object[] { getAttributeDescription(attribute), target.getName(), Double.valueOf(result) }), false);
/* 158 */     return (int)(result * scale);
/*     */   }
/*     */   
/*     */   private static int getAttributeBase(CommandSourceStack source, Entity target, Holder<Attribute> attribute, double scale) throws CommandSyntaxException {
/* 162 */     LivingEntity livingEntity = getEntityWithAttribute(target, attribute);
/* 163 */     double result = livingEntity.getAttributeBaseValue(attribute);
/* 164 */     source.sendSuccess(() -> Component.translatable("commands.attribute.base_value.get.success", new Object[] { getAttributeDescription(attribute), target.getName(), Double.valueOf(result) }), false);
/* 165 */     return (int)(result * scale);
/*     */   }
/*     */   
/*     */   private static int getAttributeModifier(CommandSourceStack source, Entity target, Holder<Attribute> attribute, Identifier id, double scale) throws CommandSyntaxException {
/* 169 */     LivingEntity livingEntity = getEntityWithAttribute(target, attribute);
/*     */     
/* 171 */     AttributeMap attributes = livingEntity.getAttributes();
/*     */     
/* 173 */     if (!attributes.hasModifier(attribute, id)) {
/* 174 */       throw ERROR_NO_SUCH_MODIFIER.create(target.getName(), getAttributeDescription(attribute), id);
/*     */     }
/*     */     
/* 177 */     double result = attributes.getModifierValue(attribute, id);
/* 178 */     source.sendSuccess(() -> Component.translatable("commands.attribute.modifier.value.get.success", new Object[] { Component.translationArg(id), getAttributeDescription(attribute), target.getName(), Double.valueOf(result) }), false);
/* 179 */     return (int)(result * scale);
/*     */   }
/*     */   
/*     */   private static Stream<Identifier> getAttributeModifiers(Entity target, Holder<Attribute> attribute) throws CommandSyntaxException {
/* 183 */     AttributeInstance attributeInstance = getAttributeInstance(target, attribute);
/* 184 */     return attributeInstance.getModifiers().stream().map(AttributeModifier::id);
/*     */   }
/*     */   
/*     */   private static int setAttributeBase(CommandSourceStack source, Entity target, Holder<Attribute> attribute, double value) throws CommandSyntaxException {
/* 188 */     getAttributeInstance(target, attribute).setBaseValue(value);
/* 189 */     source.sendSuccess(() -> Component.translatable("commands.attribute.base_value.set.success", new Object[] { getAttributeDescription(attribute), target.getName(), Double.valueOf(value) }), false);
/* 190 */     return 1;
/*     */   }
/*     */   
/*     */   private static int resetAttributeBase(CommandSourceStack source, Entity target, Holder<Attribute> attribute) throws CommandSyntaxException {
/* 194 */     LivingEntity livingTarget = getLivingEntity(target);
/* 195 */     if (!livingTarget.getAttributes().resetBaseValue(attribute)) {
/* 196 */       throw ERROR_NO_SUCH_ATTRIBUTE.create(target.getName(), getAttributeDescription(attribute));
/*     */     }
/* 198 */     double value = livingTarget.getAttributeBaseValue(attribute);
/* 199 */     source.sendSuccess(() -> Component.translatable("commands.attribute.base_value.reset.success", new Object[] { getAttributeDescription(attribute), target.getName(), Double.valueOf(value) }), false);
/* 200 */     return 1;
/*     */   }
/*     */   
/*     */   private static int addModifier(CommandSourceStack source, Entity target, Holder<Attribute> attribute, Identifier id, double value, AttributeModifier.Operation operation) throws CommandSyntaxException {
/* 204 */     AttributeInstance attributeInstance = getAttributeInstance(target, attribute);
/* 205 */     AttributeModifier modifier = new AttributeModifier(id, value, operation);
/* 206 */     if (attributeInstance.hasModifier(id)) {
/* 207 */       throw ERROR_MODIFIER_ALREADY_PRESENT.create(target.getName(), getAttributeDescription(attribute), id);
/*     */     }
/* 209 */     attributeInstance.addPermanentModifier(modifier);
/* 210 */     source.sendSuccess(() -> Component.translatable("commands.attribute.modifier.add.success", new Object[] { Component.translationArg(id), getAttributeDescription(attribute), target.getName() }), false);
/* 211 */     return 1;
/*     */   }
/*     */   
/*     */   private static int removeModifier(CommandSourceStack source, Entity target, Holder<Attribute> attribute, Identifier id) throws CommandSyntaxException {
/* 215 */     AttributeInstance attributeInstance = getAttributeInstance(target, attribute);
/* 216 */     if (attributeInstance.removeModifier(id)) {
/* 217 */       source.sendSuccess(() -> Component.translatable("commands.attribute.modifier.remove.success", new Object[] { Component.translationArg(id), getAttributeDescription(attribute), target.getName() }), false);
/* 218 */       return 1;
/*     */     } 
/* 220 */     throw ERROR_NO_SUCH_MODIFIER.create(target.getName(), getAttributeDescription(attribute), id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 225 */   private static Component getAttributeDescription(Holder<Attribute> attribute) { return Component.translatable(((Attribute)attribute.value()).getDescriptionId()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\AttributeCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */