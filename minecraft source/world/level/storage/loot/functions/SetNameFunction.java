/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.UnaryOperator;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SetNameFunction extends LootItemConditionalFunction {
/*  30 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  32 */   public static final MapCodec<SetNameFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(ComponentSerialization.CODEC
/*  33 */           .optionalFieldOf("name").forGetter(()), LootContext.EntityTarget.CODEC
/*  34 */           .optionalFieldOf("entity").forGetter(()), Target.CODEC
/*  35 */           .optionalFieldOf("target", Target.CUSTOM_NAME).forGetter(())))
/*  36 */       .apply(i, SetNameFunction::new));
/*     */   
/*     */   private final Optional<Component> name;
/*     */   private final Optional<LootContext.EntityTarget> resolutionContext;
/*     */   private final Target target;
/*     */   
/*     */   private SetNameFunction(List<LootItemCondition> predicates, Optional<Component> name, Optional<LootContext.EntityTarget> resolutionContext, Target target) {
/*  43 */     super(predicates);
/*  44 */     this.name = name;
/*  45 */     this.resolutionContext = resolutionContext;
/*  46 */     this.target = target;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  51 */   public LootItemFunctionType<SetNameFunction> getType() { return LootItemFunctions.SET_NAME; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   public Set<ContextKey<?>> getReferencedContextParams() { return (Set)this.resolutionContext.map(target -> Set.of(target.contextParam())).orElse(Set.of()); }
/*     */ 
/*     */   
/*     */   public static UnaryOperator<Component> createResolver(LootContext context, LootContext.EntityTarget entityTarget) {
/*  60 */     if (entityTarget != null) {
/*  61 */       Entity entity = (Entity)context.getOptionalParameter(entityTarget.contextParam());
/*  62 */       if (entity != null) {
/*     */ 
/*     */         
/*  65 */         CommandSourceStack commandSourceStack = entity.createCommandSourceStackForNameResolution(context.getLevel()).withPermission(LevelBasedPermissionSet.GAMEMASTER);
/*  66 */         return line -> {
/*     */             try {
/*  68 */               return ComponentUtils.updateForEntity(commandSourceStack, line, entity, 0);
/*  69 */             } catch (CommandSyntaxException e) {
/*  70 */               LOGGER.warn("Failed to resolve text component", e);
/*  71 */               return line;
/*     */             } 
/*     */           };
/*     */       } 
/*     */     } 
/*  76 */     return line -> line;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack itemStack, LootContext context) {
/*  81 */     this.name.ifPresent(name -> itemStack.set(this.target.component(), (Component)createResolver(context, (LootContext.EntityTarget)this.resolutionContext.orElse(null)).apply(name)));
/*  82 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/*  86 */   public static LootItemConditionalFunction.Builder<?> setName(Component value, Target target) { return simpleBuilder(conditions -> new SetNameFunction(conditions, Optional.of(value), Optional.empty(), target)); }
/*     */ 
/*     */ 
/*     */   
/*  90 */   public static LootItemConditionalFunction.Builder<?> setName(Component value, Target target, LootContext.EntityTarget resolutionContext) { return simpleBuilder(conditions -> new SetNameFunction(conditions, Optional.of(value), Optional.of(resolutionContext), target)); }
/*     */   
/*     */   public enum Target
/*     */     implements StringRepresentable {
/*  94 */     CUSTOM_NAME("custom_name"),
/*  95 */     ITEM_NAME("item_name"); public static final Codec<Target> CODEC; private final String name;
/*     */     
/*     */     static  {
/*  98 */       CODEC = StringRepresentable.fromEnum(Target::values);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 103 */     Target(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */     
/*     */     public DataComponentType<Component> component() {
/* 113 */       switch (ordinal()) { default: throw new MatchException(null, null);case 1: case 0: break; }  return 
/*     */         
/* 115 */         DataComponents.CUSTOM_NAME;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetNameFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */