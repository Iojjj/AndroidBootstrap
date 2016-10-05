package com.github.iojjj.bootstrap.widgets.aspect_ratio_view;

interface AspectRatio {

    /**
     * Default value of aspect ratios.
     */
    float RATIO_NOT_SET = -1.0f;

    /**
     * Get aspect ratio Y.
     *
     * @return aspect ratio X
     *
     * @see #RATIO_NOT_SET
     */
    float getRatioX();

    /**
     * Get aspect ratio Y.
     *
     * @return aspect ratio Y
     *
     * @see #RATIO_NOT_SET
     */
    float getRatioY();

    /**
     * Set aspect for this view.
     *
     * @param ratioX aspect ratio X
     * @param ratioY aspect ratio Y
     *
     * @see #RATIO_NOT_SET
     */
    void setAspectRatio(float ratioX, float ratioY);
}
