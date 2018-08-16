package org.yggard.brokkgui.skin;

import fr.ourten.teabeans.binding.BaseBinding;
import org.yggard.brokkgui.behavior.GuiScrollableBehavior;
import org.yggard.brokkgui.control.GuiScrollableBase;
import org.yggard.brokkgui.policy.EScrollbarPolicy;
import org.yggard.brokkgui.shape.Rectangle;

/**
 * @author Ourten 9 oct. 2016
 */
public class GuiScrollableSkin<C extends GuiScrollableBase, B extends GuiScrollableBehavior<C>> extends
        GuiBehaviorSkinBase<C, B>
{
    private final Rectangle gripX, gripY;

    public GuiScrollableSkin(C model, B behavior)
    {
        super(model, behavior);

        this.gripX = new Rectangle();
        this.gripX.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getxPosProperty(),
                        getModel().getxTranslateProperty(),
                        getModel().getScrollXProperty(),
                        getModel().getTrueWidthProperty(),
                        getModel().getWidthProperty(),
                        gripX.getWidthProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().getWidth() >= getModel().getTrueWidth())
                    return getModel().getxPos() + getModel().getxTranslate();
                else
                {
                    float area = getModel().getTrueWidth() - getModel().getWidth();
                    float ratio = getModel().getScrollX() / area;
                    float size = getModel().getWidth() - gripX.getWidth();

                    return getModel().getxPos() + getModel().getxTranslate() - size * ratio;
                }
            }
        });
        this.gripX.getyPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getyPosProperty(),
                        getModel().getyTranslateProperty(),
                        getModel().getHeightProperty(),
                        gripX.getHeightProperty());
            }

            @Override
            public Float computeValue()
            {
                return getModel().getyPos() + getModel().getyTranslate() + getModel().getHeight() - gripX.getHeight();
            }
        });
        this.gripX.getHeightProperty().bind(this.getModel().getGripXHeightProperty());
        this.gripX.getWidthProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getWidthProperty(),
                        getModel().getTrueWidthProperty(),
                        getModel().getGripXWidthProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().getGripXWidth() != 0)
                    return getModel().getGripXWidth();

                if (getModel().getWidth() >= getModel().getTrueWidth())
                    return getModel().getWidth();
                return Math.min(10, getModel().getWidth() / getModel().getTrueWidth() * getModel().getWidth());
            }
        });
        this.gripX.setzLevel(10);

        this.gripY = new Rectangle();
        this.gripY.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getxPosProperty(),
                        getModel().getxTranslateProperty(),
                        getModel().getWidthProperty(),
                        gripY.getWidthProperty());
            }

            @Override
            public Float computeValue()
            {
                return getModel().getxPos() + getModel().getxTranslate() + getModel().getWidth() - gripY.getWidth();
            }
        });
        this.gripY.getyPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getyPosProperty(),
                        getModel().getyTranslateProperty(),
                        getModel().getScrollYProperty(),
                        getModel().getTrueHeightProperty(),
                        getModel().getHeightProperty(),
                        gripY.getHeightProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().getHeight() >= getModel().getTrueHeight())
                    return getModel().getyPos()
                            + getModel().getyTranslate();
                else
                {
                    float area = getModel().getTrueHeight() - getModel().getHeight();
                    float ratio = getModel().getScrollY() / area;
                    float size = getModel().getHeight() - gripY.getHeight();

                    return getModel().getyPos() + getModel().getyTranslate() - size * ratio;
                }
            }
        });
        this.gripY.getWidthProperty().bind(this.getModel().getGripYWidthProperty());
        this.gripY.getHeightProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getHeightProperty(),
                        getModel().getTrueHeightProperty(),
                        getModel().getGripYHeightProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().getGripYHeight() != 0)
                    return getModel().getGripYHeight();

                if (getModel().getHeight() >= getModel().getTrueHeight())
                    return getModel().getHeight();
                return Math.min(10, getModel().getHeight() / getModel().getTrueHeight() * getModel().getHeight());
            }
        });

        this.getModel().addChild(this.gripX);
        this.getModel().addChild(this.gripY);

        this.gripX.addStyleClass("grip-x");
        this.gripY.addStyleClass("grip-y");

        this.gripX.getVisibleProperty().bind(new BaseBinding<Boolean>()
        {
            {
                super.bind(getModel().getScrollXPolicyProperty(),
                        getModel().getWidthProperty(),
                        getModel().getTrueWidthProperty());
            }

            @Override
            public Boolean computeValue()
            {
                if (getModel().getScrollXPolicy() == EScrollbarPolicy.ALWAYS
                        || getModel().getScrollXPolicy() == EScrollbarPolicy.NEEDED)
                {
                    if (getModel().getWidth() >= getModel().getTrueWidth())
                        return getModel().getScrollXPolicy() == EScrollbarPolicy.ALWAYS;
                    else
                        return true;
                }
                return false;
            }
        });

        this.gripY.getVisibleProperty().bind(new BaseBinding<Boolean>()
        {
            {
                super.bind(getModel().getScrollYPolicyProperty(),
                        getModel().getHeightProperty(),
                        getModel().getTrueHeightProperty());
            }

            @Override
            public Boolean computeValue()
            {
                if (getModel().getScrollYPolicy() == EScrollbarPolicy.ALWAYS
                        || getModel().getScrollYPolicy() == EScrollbarPolicy.NEEDED)
                {
                    if (getModel().getHeight() >= getModel().getTrueHeight())
                        return getModel().getScrollYPolicy() == EScrollbarPolicy.ALWAYS;
                    else
                        return true;
                }
                return false;
            }
        });
    }
}
