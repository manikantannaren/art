mkdir photos-Optimized;for photos in *.JPG;do convert -verbose "$photos" -quality 50% -resize 1600x900 ./photos-Optimized/"$photos-Optimized.jpg"; done
