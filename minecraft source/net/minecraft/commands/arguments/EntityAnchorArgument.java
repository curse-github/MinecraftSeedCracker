/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class EntityAnchorArgument extends Object implements ArgumentType<EntityAnchorArgument.Anchor> {
/* 26 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "eyes", "feet" });
/* 27 */   private static final DynamicCommandExceptionType ERROR_INVALID = new DynamicCommandExceptionType(name -> Component.translatableEscape("argument.anchor.invalid", new Object[] { name }));
/*    */ 
/*    */   
/* 30 */   public static Anchor getAnchor(CommandContext<CommandSourceStack> context, String name) { return (Anchor)context.getArgument(name, Anchor.class); }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public static EntityAnchorArgument anchor() { return new EntityAnchorArgument(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Anchor parse(StringReader reader) throws CommandSyntaxException {
/* 39 */     int start = reader.getCursor();
/* 40 */     String name = reader.readUnquotedString();
/* 41 */     Anchor anchor = Anchor.getByName(name);
/* 42 */     if (anchor == null) {
/* 43 */       reader.setCursor(start);
/* 44 */       throw ERROR_INVALID.createWithContext(reader, name);
/*    */     } 
/* 46 */     return anchor;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) { return SharedSuggestionProvider.suggest(Anchor.BY_NAME.keySet(), builder); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */   
/*    */   public enum Anchor
/*    */   {
/* 60 */     FEET("feet", (p, e) -> p),
/* 61 */     EYES("eyes", (p, e) -> new Vec3(p.x, p.y + e.getEyeHeight(), p.z));
/*    */     
/*    */     static  {
/* 64 */       BY_NAME = (Map)Util.make(Maps.newHashMap(), map -> {
/* 65 */             for (Anchor anchor : values())
/* 66 */               map.put(anchor.name, anchor); 
/*    */           });
/*    */     }
/*    */     private static final Map<String, Anchor> BY_NAME;
/*    */     private final String name;
/*    */     private final BiFunction<Vec3, Entity, Vec3> transform;
/*    */     
/*    */     Anchor(String name, BiFunction<Vec3, Entity, Vec3> transform) {
/* 74 */       this.name = name;
/* 75 */       this.transform = transform;
/*    */     }
/*    */ 
/*    */     
/* 79 */     public static Anchor getByName(String name) { return (Anchor)BY_NAME.get(name); }
/*    */ 
/*    */ 
/*    */     
/* 83 */     public Vec3 apply(Entity entity) { return (Vec3)this.transform.apply(entity.position(), entity); }
/*    */ 
/*    */     
/*    */     public Vec3 apply(CommandSourceStack source) {
/* 87 */       Entity entity = source.getEntity();
/* 88 */       if (entity == null) {
/* 89 */         return source.getPosition();
/*    */       }
/* 91 */       return (Vec3)this.transform.apply(source.getPosition(), entity);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\EntityAnchorArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */