/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ComponentContents
/*    */ {
/* 13 */   default <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style currentStyle) { return Optional.empty(); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   default <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) { return Optional.empty(); }
/*    */ 
/*    */ 
/*    */   
/* 21 */   default MutableComponent resolve(CommandSourceStack source, Entity entity, int recursionDepth) throws CommandSyntaxException { return MutableComponent.create(this); }
/*    */   
/*    */   MapCodec<? extends ComponentContents> codec();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\ComponentContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */