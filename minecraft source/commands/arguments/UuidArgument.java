/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.UUID;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class UuidArgument
/*    */   extends Object implements ArgumentType<UUID> {
/* 18 */   public static final SimpleCommandExceptionType ERROR_INVALID_UUID = new SimpleCommandExceptionType(Component.translatable("argument.uuid.invalid"));
/*    */   
/* 20 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "dd12be42-52a9-4a91-a8a1-11c01849e498" });
/*    */   
/* 22 */   private static final Pattern ALLOWED_CHARACTERS = Pattern.compile("^([-A-Fa-f0-9]+)");
/*    */ 
/*    */   
/* 25 */   public static UUID getUuid(CommandContext<CommandSourceStack> source, String name) { return (UUID)source.getArgument(name, UUID.class); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public static UuidArgument uuid() { return new UuidArgument(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public UUID parse(StringReader reader) throws CommandSyntaxException {
/* 34 */     String remaining = reader.getRemaining();
/* 35 */     Matcher matcher = ALLOWED_CHARACTERS.matcher(remaining);
/* 36 */     if (matcher.find()) {
/* 37 */       String maybeUUID = matcher.group(1);
/*    */       try {
/* 39 */         UUID result = UUID.fromString(maybeUUID);
/* 40 */         reader.setCursor(reader.getCursor() + maybeUUID.length());
/* 41 */         return result;
/* 42 */       } catch (IllegalArgumentException illegalArgumentException) {}
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 47 */     throw ERROR_INVALID_UUID.createWithContext(reader);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\UuidArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */