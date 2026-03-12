/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.world.level.block.Mirror;
/*    */ 
/*    */ public class TemplateMirrorArgument
/*    */   extends StringRepresentableArgument<Mirror> {
/*  9 */   private TemplateMirrorArgument() { super(Mirror.CODEC, Mirror::values); }
/*    */ 
/*    */ 
/*    */   
/* 13 */   public static StringRepresentableArgument<Mirror> templateMirror() { return new TemplateMirrorArgument(); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static Mirror getMirror(CommandContext<CommandSourceStack> context, String name) { return (Mirror)context.getArgument(name, Mirror.class); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\TemplateMirrorArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */