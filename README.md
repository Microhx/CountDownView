#### CountDownView
一款小巧的自定义计时View

使用:<br/>
  > implementation 'com.xing.lib:CountDownView:0.0.9'

效果如下：<br/>
<img src='https://raw.githubusercontent.com/Microhx/CountDownView/master/img/demo.gif' width="340"/>

可以自定的属性为:


| 自定义属性 | 含义 |
| ------ | ------ |
| hx_bg_color | 大圆背景颜色 |
| hx_arc_color | 圆环颜色|
| hx_arc_width   |  圆环宽度 |
| hx_arc_padding |  圆环padding(默认为1dp)        |
| hx_text_change_style      |  动画的方式 数值增加/减小     |
| hx_text_size  | 文字大小    |
| hx_max_time   | 显示最大时间   |
| hx_time_unit      |   时间单位    |
| hx_start_angle|  起始滑动的角度(默认是-90,顶点上开始转)  |
| hx_bg_radius     | 大圆的半径(默认是30dp)      |
| hx_just_skip  | 不显示数字 只显示‘跳过’ |
| hx_skip_text | 不显示数字时显示的文字(默认为‘跳过’) |
| hx_skip_text_size | ‘跳过’时 文字大小 |
| hx_skip_text_color | ‘跳过’时  文字颜色|

```
//start
countDownView.start()

//finish Listener
countDownView.setCountDownListener(new OnCountDownViewListener(){
  public void onFinished(){
      //TODO what you want...
  }
});

```
