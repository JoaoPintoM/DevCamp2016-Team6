class Api::UserSpotLinksController < Api::BaseController
  def create
    if @user.present?
      @mission        = Mission.find(params[:mission_id])
      @spot           = @mission.spots.find(params[:spot_id])
      @user_spot_link = @spot.user_spot_links.where(:user_id => @user.id).first_or_initialize

      if params[:user_spot_link][:picture]
        Rails.logger.info "je suis dans le if"

        tmp_path = "#{Rails.root}/tmp/#{UUIDTools::UUID.random_create}.jpg"
        data     = Base64.decode64(params[:user_spot_link][:picture])

        Rails.logger.info tmp_path

        File.open(tmp_path, 'wb') do |file|
          file.write(data)
        end

        @user_spot_link.picture = File.new(tmp_path)
        @user_spot_link.save!
        FileUtils.rm(tmp_path)
      else
        Rails.logger.info "je suis dans le else"
        @user_spot_link.destroy! if @user_spot_link.persisted?
      end
    end

    render :json => { :success => true }
  end
end
